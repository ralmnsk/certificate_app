package com.epam.esm.web.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CustomPageDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.filter.CertificateFilterDto;
import com.epam.esm.dto.filter.OrderFilterDto;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.repository.exception.NotFoundException;
import com.epam.esm.repository.exception.SaveException;
import com.epam.esm.repository.exception.UpdateException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.web.assembler.OrderAssembler;
import com.epam.esm.web.page.CertificatePageBuilder;
import com.epam.esm.web.page.OrderPageBuilder;
import com.epam.esm.web.security.config.WebSecurity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.security.Principal;
import java.util.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderService orderService;
    private OrderAssembler orderAssembler;
    private OrderPageBuilder orderPageBuilder;
    private CertificatePageBuilder certificatePageBuilder;
    private CertificateService certificateService;
    private WebSecurity webSecurity;
    private UserService userService;
    private ObjectMapper objectMapper;

    public OrderController(OrderService orderService, OrderAssembler orderAssembler, OrderPageBuilder orderPageBuilder, CertificatePageBuilder certificatePageBuilder, CertificateService certificateService, WebSecurity webSecurity, UserService userService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.orderAssembler = orderAssembler;
        this.orderPageBuilder = orderPageBuilder;
        this.certificatePageBuilder = certificatePageBuilder;
        this.certificateService = certificateService;
        this.webSecurity = webSecurity;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@Valid @RequestBody OrderDto orderDto, Authentication authentication) {

        String login = authentication.getName();
        if (login == null) {
            throw new SaveException("you have no right to create order");
        }
        UserDto user = userService.findByLogin(login);
        if (user == null) {
            throw new NotFoundException("Create order: user not found exception");
        }
        orderDto = orderService.save(orderDto).orElseThrow(() -> new SaveException("Order save exception"));
        Set<Long> set = new HashSet<>();
        set.add(orderDto.getId());
        orderService.addOrderToUser(user.getId(), set);
        return orderAssembler.assemble(orderDto.getId(), orderDto, authentication);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto get(@PathVariable Long id, Authentication authentication) {
        String login = authentication.getName();
        webSecurity.checkOrderId(login, id);
        OrderDto orderDto = orderService.get(id).orElseThrow(() -> new NotFoundException(id));
        return orderAssembler.assemble(id, orderDto, authentication);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto update(@Valid @RequestBody OrderDto orderDto, @PathVariable Long id, Authentication authentication) {
        String login = authentication.getName();
        webSecurity.checkOrderId(login, id);
        orderDto.setId(id);
        orderDto = orderService.update(orderDto).orElseThrow(() -> new NotFoundException(id));
        return orderAssembler.assemble(id, orderDto, authentication);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Principal principal) {
        webSecurity.checkOrderId(principal, id);
        orderService.delete(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CustomPageDto<OrderDto> getAll(
            @RequestParam(value = "surname", defaultValue = "")
            @Size(max = 16, message = "Surname should be 0-16 characters") String surname,

            @RequestParam(value = "userName", defaultValue = "")
            @Size(max = 16, message = "Name should be 0-16 characters") String userName,

            @RequestParam(value = "certificateName", defaultValue = "")
            @Size(max = 16, message = "Certificate name should be 0-16 characters") String certificateName,

            @RequestParam(value = "page", defaultValue = "0")
            @Min(value = 0, message = "page must be 0-10000000")
            @Max(value = 10000000, message = "page must be 0-10000000") int page,

            @RequestParam(value = "size", defaultValue = "5")
            @Min(value = 1, message = "size must be 1-100")
            @Max(value = 100, message = "size must be 1-100") int size,
            @RequestParam(required = false) List<String> sort,
            Principal principal
    ) {
        webSecurity.checkOperationAccess(principal);
        OrderFilterDto filter = new OrderFilterDto();
        filter.setUserSurname(surname);
        filter.setUserName(userName);
        filter.setCertificateName(certificateName);
        filter.setPage(page);
        filter.setSize(size);
        filter.setSortParams(sort);
        CustomPageDto<OrderDto> build = orderPageBuilder.build(filter);

        return build;
    }


    @GetMapping("/{orderId}/certificates")
    @ResponseStatus(HttpStatus.OK)
    public CustomPageDto<CertificateDto> getAllCertificatesByOrderId(
            @RequestParam(value = "certificateName", defaultValue = "")
            @Size(max = 16, message = "certificate name should be 0-16 characters") String certificateName,

            @RequestParam(value = "page", defaultValue = "0")
            @Min(value = 0, message = "page must be 0-10000000")
            @Max(value = 10000000, message = "page must be 0-10000000") int page,

            @RequestParam(value = "size", defaultValue = "5")
            @Min(value = 1, message = "size must be 1-100")
            @Max(value = 100, message = "size must be 1-100") int size,

            @RequestParam(required = false) List<String> sort,

            @PathVariable Long orderId,
            Principal principal
    ) {
        webSecurity.checkOrderId(principal, orderId);
        CertificateFilterDto filterDto = new CertificateFilterDto();
        filterDto.setCertificateName(certificateName);
        filterDto.setPage(page);
        filterDto.setSize(size);
        filterDto.setSortParams(sort);
        filterDto.setOrderId(orderId);

        return certificatePageBuilder.build(filterDto);
    }

    @PutMapping("/{orderId}/certificates")
    @ResponseStatus(HttpStatus.OK)
    public CustomPageDto<CertificateDto> addCertificateToOrder(@PathVariable Long orderId, @Valid @RequestBody Set<Long> certificateIds,
                                                               Principal principal) {
        webSecurity.checkOrderId(principal, orderId);
        certificateService.addCertificateToOrder(orderId, certificateIds);

        CertificateFilterDto filterDto = new CertificateFilterDto();
        filterDto.setOrderId(orderId);


        return certificatePageBuilder.build(filterDto);
    }

    @DeleteMapping("/{orderId}/certificates")
    @ResponseStatus(HttpStatus.OK)
    public CustomPageDto<CertificateDto> deleteCertificateFromOrder(@PathVariable Long orderId, @Valid @RequestBody Set<Long> set,
                                                                    Principal principal) {
        webSecurity.checkOrderId(principal, orderId);
        certificateService.removeCertificateFromOrder(orderId, set);

        CertificateFilterDto filterDto = new CertificateFilterDto();
        filterDto.setOrderId(orderId);

        return certificatePageBuilder.build(filterDto);
    }

    @PatchMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public OrderDto update(@PathVariable Long id, @RequestBody JsonPatch patch, Authentication authentication) {
        String principal = authentication.getName();
        webSecurity.checkOrderId(principal, id);

        OrderDto orderDto = orderService.get(id).orElseThrow(() -> new NotFoundException(id));
        OrderDto orderDtoPatched = null;
        try {
            orderDtoPatched = applyPatchToOrder(patch, orderDto);
        } catch (JsonPatchException e) {
            log.warn("Order patch exception:{}", e.getMessage());
            throw new UpdateException("Certificate patch processing exception:" + e.getMessage());
        } catch (JsonProcessingException e) {
            log.warn("Order patch processing exception:{}", e.getMessage());
            throw new UpdateException("Order patch processing exception:" + e.getMessage());
        }
        orderDto = patchToDto(orderDto, orderDtoPatched);
        orderDto = orderService.update(orderDto).orElseThrow(() -> new UpdateException(id));

        return orderAssembler.assemble(id, orderDto, authentication);
    }

    private OrderDto applyPatchToOrder(JsonPatch patch, OrderDto orderDtoDto) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(orderDtoDto, JsonNode.class));
        return objectMapper.treeToValue(patched, OrderDto.class);
    }

    private OrderDto patchToDto(OrderDto dto, OrderDto patched) {
        Map<String, String> errors = new HashMap<>();

        if (patched.getDescription() != null) {
            boolean matches = patched.getDescription().matches("([А-Яа-яa-zA-Z0-9- .!&?#,;$]){0,999}");
            if (matches) {
                dto.setDescription(patched.getDescription());
            } else {
                errors.put("description:", "Description must be between 0 and 999 characters.");
            }
        }

        if (patched.isCompleted() == true) {
            dto.setCompleted(patched.isCompleted());
        }

        if (!errors.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            errors.forEach((k, v) -> {
                builder.append("Field ").append(k).append(v).append("  ");
            });
            log.error(builder.toString());
            ValidationException validationException = new ValidationException(builder.toString());
            validationException.getFieldsException().putAll(errors);
            errors.clear();
            throw validationException;
        }

        return dto;
    }

}
