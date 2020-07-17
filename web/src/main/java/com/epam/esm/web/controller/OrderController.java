package com.epam.esm.web.controller;

import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.CustomPageDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.filter.CertificateFilterDto;
import com.epam.esm.service.dto.filter.OrderFilterDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.service.user.UserService;
import com.epam.esm.web.assembler.OrderAssembler;
import com.epam.esm.web.page.CertificatePageBuilder;
import com.epam.esm.web.page.OrderPageBuilder;
import com.epam.esm.web.security.config.WebSecurity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public OrderController(OrderService orderService, OrderAssembler orderAssembler,
                           OrderPageBuilder orderPageBuilder,
                           CertificatePageBuilder certificatePageBuilder,
                           CertificateService certificateService, WebSecurity webSecurity,
                           UserService userService) {
        this.orderService = orderService;
        this.orderAssembler = orderAssembler;
        this.orderPageBuilder = orderPageBuilder;
        this.certificatePageBuilder = certificatePageBuilder;
        this.certificateService = certificateService;
        this.webSecurity = webSecurity;
        this.userService = userService;
    }

    @PostMapping
    public OrderDto create(@Valid @RequestBody OrderDto orderDto, Principal principal) {
        String login = principal.getName();
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
        return orderAssembler.assemble(orderDto.getId(), orderDto);
    }

    @GetMapping("/{id}")
    public OrderDto get(@PathVariable Long id, Principal principal) {
        webSecurity.checkOrderId(principal, id);
        OrderDto orderDto = orderService.get(id).orElseThrow(() -> new NotFoundException(id));
        return orderAssembler.assemble(id, orderDto);
    }

    @PutMapping("/{id}")
    public OrderDto update(@Valid @RequestBody OrderDto orderDto, @PathVariable Long id, Principal principal) {
        webSecurity.checkOrderId(principal, id);
        orderDto.setId(id);
        orderDto = orderService.update(orderDto).orElseThrow(() -> new NotFoundException(id));
        return orderAssembler.assemble(id, orderDto);
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
            @Min(0)
            @Max(10000000) int page,

            @RequestParam(value = "size", defaultValue = "5")
            @Min(1)
            @Max(100) int size,
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
            @Min(0)
            @Max(10000000) int page,

            @RequestParam(value = "size", defaultValue = "5")
            @Min(1)
            @Max(100) int size,

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
    public CustomPageDto<CertificateDto> addCertificateToOrder(@PathVariable Long orderId, @Valid @RequestBody Set<Long> set,
                                                               Principal principal) {
        webSecurity.checkOrderId(principal, orderId);
        certificateService.addCertificateToOrder(orderId, set);

        CertificateFilterDto filterDto = new CertificateFilterDto();
        filterDto.setOrderId(orderId);


        return certificatePageBuilder.build(filterDto);
    }

    @DeleteMapping("/{orderId}/certificates")
    @ResponseStatus(HttpStatus.OK)
    public CustomPageDto<CertificateDto> deleteCertificateFromOrder(@PathVariable Long orderId, @Valid @RequestBody Set<Long> set,
                                                                    Principal principal) {
        webSecurity.checkOrderId(principal, orderId);
        certificateService.deleteCertificateFromOrder(orderId, set);

        CertificateFilterDto filterDto = new CertificateFilterDto();
        filterDto.setOrderId(orderId);


        return certificatePageBuilder.build(filterDto);
    }

}
