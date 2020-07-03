package com.epam.esm.web.controller;

import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.web.assembler.CertificateAssembler;
import com.epam.esm.web.assembler.OrderAssembler;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

import static com.epam.esm.web.controller.ControllerConstants.*;

@Validated
@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderService<OrderDto, Long> orderService;
    private CertificateService<CertificateDto, Long> certService;
    private OrderAssembler orderAssembler;
    private CertificateAssembler certificateAssembler;

    public OrderController(OrderService<OrderDto, Long> orderService, CertificateService<CertificateDto, Long> certService, OrderAssembler orderAssembler, CertificateAssembler certificateAssembler) {
        this.orderService = orderService;
        this.certService = certService;
        this.orderAssembler = orderAssembler;
        this.certificateAssembler = certificateAssembler;
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
    public CollectionModel<OrderDto> getAll(@PageableDefault(page = DEFAULT_PAGE_NUMBER,
            size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        List<OrderDto> orders = orderService.getAll(pageable).getContent();

//        orders.forEach(o -> {
//            Link link = linkTo(methodOn(OrderController.class).get(o.getId())).withSelfRel();
//            Link linkCerts = linkTo(methodOn(OrderController.class).getAll(pageable, o.getId())).withRel("certificates");
//            o.add(link);
//            o.add(linkCerts);
//        });
//        Link linkOrders = linkTo(methodOn(OrderController.class).getAll(pageable)).withRel("orders");
//
//
//        return CollectionModel.of(orders, linkOrders);
        return orderAssembler.toCollectionModel(PARAM_NOT_USED, orders, pageable);
    }


    @GetMapping("/{orderId}/certificates")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<CertificateDto> getAll(@PageableDefault(page = DEFAULT_PAGE_NUMBER,
            size = DEFAULT_PAGE_SIZE) Pageable pageable, @PathVariable Long orderId) {
        List<CertificateDto> certificates = certService.getAllByOrderId(orderId, pageable).getContent();
//        if (!certificates.isEmpty()) {
//            certificates.forEach(c -> {
//                Link selfLink = linkTo(methodOn(CertificateController.class).get(c.getId())).withSelfRel();
//                c.add(selfLink);
//            });
//        }
//
//        Link link = linkTo(methodOn(OrderController.class).getAll(pageable, orderId)).withSelfRel();
//
//        return CollectionModel.of(certificates, link);
        return certificateAssembler.toCollectionModel(orderId, certificates, pageable);
    }

    @PostMapping("/{orderId}/certificates")
    @ResponseStatus(HttpStatus.OK)
    public CertificateDto createCertificateInOrder(@PageableDefault(page = DEFAULT_PAGE_NUMBER,
            size = DEFAULT_PAGE_SIZE, sort = DEFAULT_SORT_ORDERS) Pageable pageable, @PathVariable Long orderId, @Valid @RequestBody CertificateDto certificateDto) {

        certificateDto = certService.createCertificateInOrder(orderId, certificateDto).orElseThrow(() -> new SaveException("Create Certificate in Order Exception"));

//        Link selfLink = linkTo(methodOn(CertificateController.class).get(certificateDto.getId())).withSelfRel();
//        certificateDto.add(selfLink);
//        Link userLink = linkTo(methodOn(OrderController.class).get(orderId)).withRel("order");
//        certificateDto.add(userLink);
//        Link certificatesLink = linkTo(methodOn(CertificateController.class).getAll(pageable, orderId)).withRel("orders");
//        certificateDto.add(certificatesLink);
//
//        return certificateDto;
        return certificateAssembler.assemble(certificateDto.getId(), certificateDto);
    }

}
