package com.epam.esm.web.controller;

import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.CustomPageDto;
import com.epam.esm.service.dto.FilterDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.web.assembler.OrderAssembler;
import com.epam.esm.web.page.CertificatePageBuilder;
import com.epam.esm.web.page.OrderPageBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderService<OrderDto, Long> orderService;
    private OrderAssembler orderAssembler;
    private OrderPageBuilder orderPageBuilder;
    private CertificatePageBuilder certificatePageBuilder;

    public OrderController(OrderService<OrderDto, Long> orderService, OrderAssembler orderAssembler, OrderPageBuilder orderPageBuilder, CertificatePageBuilder certificatePageBuilder) {
        this.orderService = orderService;
        this.orderAssembler = orderAssembler;
        this.orderPageBuilder = orderPageBuilder;
        this.certificatePageBuilder = certificatePageBuilder;
    }

    @PostMapping
    public OrderDto create(@Valid @RequestBody OrderDto orderDto) {
        orderDto = orderService.save(orderDto).orElseThrow(() -> new SaveException("Order save exception"));
        return orderAssembler.assemble(orderDto.getId(), orderDto);
    }

    @GetMapping("/{id}")
    public OrderDto get(@PathVariable Long id) {
        OrderDto orderDto = orderService.get(id).orElseThrow(() -> new NotFoundException(id));
        return orderAssembler.assemble(id, orderDto);
    }

    @PutMapping("/{id}")
    public OrderDto update(@Valid @RequestBody OrderDto orderDto, @PathVariable Long id) {
        orderDto.setId(id);
        orderDto = orderService.update(orderDto).orElseThrow(() -> new NotFoundException(id));
        return orderAssembler.assemble(id, orderDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletResponse response) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CustomPageDto<OrderDto> getAll(
            @RequestParam(value = "surname", defaultValue = "")
            @Size(max = 16, message = "Surname should be 0-16 characters") String surname,

            @RequestParam(value = "name", defaultValue = "")
            @Size(max = 16, message = "Name should be 0-16 characters") String userName,

            @RequestParam(value = "name", defaultValue = "")
            @Size(max = 16, message = "Certificate name should be 0-16 characters") String certificateName,

            @RequestParam(value = "page", defaultValue = "0")
            @Min(0)
            @Max(10000000) int page,

            @RequestParam(value = "size", defaultValue = "5")
            @Min(1)
            @Max(100) int size,
            @RequestParam(required = false) List<String> sort
    ) {
        FilterDto filterDto = new FilterDto();
        filterDto.setUserSurname(surname);
        filterDto.setUserName(userName);
        filterDto.setCertificateName(certificateName);
        filterDto.setPage(page);
        filterDto.setSize(size);
        filterDto.setSortParams(sort);
        CustomPageDto<OrderDto> build = orderPageBuilder.build(filterDto);

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

            @PathVariable Long orderId
    ) {
        FilterDto filterDto = new FilterDto();
        filterDto.setCertificateName(certificateName);
        filterDto.setPage(page);
        filterDto.setSize(size);
        filterDto.setSortParams(sort);
        filterDto.setOrderId(orderId);

        return certificatePageBuilder.build(filterDto);
    }
//
//    @PostMapping("/{orderId}/certificates")
//    @ResponseStatus(HttpStatus.OK)
//    public OrderDto addCertificateToOrder(@PathVariable Long orderId, @Valid @RequestBody List<IdDto> listIdDto) {
//        OrderDto orderDto = orderService.addCertificateToOrder(orderId, listIdDto).orElseThrow(() -> new SaveException("Create Certificate in Order Exception"));
//
//        return orderAssembler.assemble(orderDto.getId(), orderDto);
//    }

//    @PutMapping("/{orderId}/certificates")
//    @ResponseStatus(HttpStatus.OK)
//    public OrderDto removeCertificateFromOrder(@PathVariable Long orderId, @Valid @RequestBody List<IdDto> listIdDto) {
//        OrderDto orderDto = orderService.removeCertificateFromOrder(orderId, listIdDto).orElseThrow(() -> new SaveException("Create Certificate in Order Exception"));
//
//        return orderAssembler.assemble(orderDto.getId(), orderDto);
//    }

}
