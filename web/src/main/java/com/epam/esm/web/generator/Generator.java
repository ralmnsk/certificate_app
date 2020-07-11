//package com.epam.esm.web.generator;
//
//import com.epam.esm.repository.crud.OrderCrudRepository;
//import com.epam.esm.service.certificate.CertificateService;
//import com.epam.esm.service.dto.CertificateDto;
//import com.epam.esm.service.dto.OrderDto;
//import com.epam.esm.service.dto.TagDto;
//import com.epam.esm.service.dto.UserDto;
//import com.epam.esm.service.order.OrderService;
//import com.epam.esm.service.tag.TagService;
//import com.epam.esm.service.user.UserService;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.File;
//
//@RestController
//@RequestMapping("/generator")
//public class Generator {
//    private File f = new File("C:\\Users\\iland\\Downloads\\certificate_app\\web\\src\\main\\java\\com\\epam\\esm\\web\\generator\\words.txt");
//    private TagService<TagDto, Integer> tagService;
//    private UserService<UserDto, Long> userService;
//    private CertificateService<CertificateDto, Long> certificateService;
//    private OrderService<OrderDto, Long> orderService;
//    private OrderCrudRepository orderRepository;
//
//    public Generator(TagService<TagDto, Integer> tagService, UserService<UserDto, Long> userService, CertificateService<CertificateDto, Long> certificateService, OrderService<OrderDto, Long> orderService) {
//        this.tagService = tagService;
//        this.userService = userService;
//        this.certificateService = certificateService;
//        this.orderService = orderService;
//    }
//
////    @GetMapping("/tags")
////    public boolean generateTags() throws IOException, InterruptedException {
////        BufferedReader br = new BufferedReader(new FileReader(f));
////
////        String st;
////        int count = 0;
////        while ((st = br.readLine()) != null && count != 1000) {
////            TagDto tag = new TagDto();
////            tag.setName(st);
////            tagService.save(tag);
////            count++;
////        }
////        br.close();
////        return true;
////    }
////
////    @GetMapping("/users")
////    public boolean generateUsers() throws IOException, InterruptedException {
////        BufferedReader br = new BufferedReader(new FileReader(f));
////
////        String st;
////        int count = 0;
////        while ((st = br.readLine()) != null && count != 1000) {
////            UserDto user = new UserDto();
////            user.setRole(Role.USER);
////            user.setPassword("pass" + st);
////            user.setLogin("login" + st);
////            user.setSurname(st);
////            user.setName("name" + st);
////            userService.save(user);
////            count++;
////        }
////        br.close();
////        return true;
////    }
////
////    @GetMapping("/certificates")
////    public boolean generateCertificates() throws IOException, InterruptedException {
////        BufferedReader br = new BufferedReader(new FileReader(f));
////
////        String st;
////        int count = 0;
////        while ((st = br.readLine()) != null && count != (10000)) {
////            if (st.length() > 2) {
////                CertificateDto certificateDto = new CertificateDto();
////                certificateDto.setName(st);
////                certificateDto.setDescription(st);
////                certificateDto.setPrice(new BigDecimal(generateInt()));
////                certificateDto.setDuration(generateSmall());
////
////                for (int i = 0; i < generateSmall(); i++) {
////                    int id = generateInt();
////                    TagDto tagDto = null;
////                    int tagOperationCount = 0;
////                    while (tagOperationCount != 1) {
////                        try {
////                            final int fId = id;
////                            tagDto = tagService.get(fId).orElseThrow(() -> new NotFoundException(fId));
////                            tagOperationCount++;
////                        } catch (Exception e) {
////                            System.out.println("Not fount id:" + id);
////                            id = generateInt();
////                        }
////                    }
////                    certificateDto.getTags().add(tagDto);
////                }
////                certificateService.save(certificateDto);
////                count++;
////            }
////        }
////        br.close();
////        return true;
////    }
////
////    @GetMapping("/orders")
////    public boolean generateOrders() throws IOException, InterruptedException {
////        BufferedReader br = new BufferedReader(new FileReader(f));
////
////        String st;
////        int count = 0;
////        while ((st = br.readLine()) != null && count != (10000)) {
////            if (st.length() > 2) {
////                OrderDto orderDto = new OrderDto();
////                orderDto.setDescription(st);
////
////                for (int i = 0; i < generateSmall(); i++) {
////                    long id = generateInt();
////                    CertificateDto certificateDto = null;
////                    int certOperationCount = 0;
////                    while (certOperationCount != 1) {
////                        try {
////                            final long fId = id;
////                            certificateDto = certificateService.get(fId).orElseThrow(() -> new NotFoundException(fId));
////                            certOperationCount++;
////                        } catch (Exception e) {
////                            System.out.println("Not fount id:" + id);
////                            id = generateInt();
////                        }
////                    }
////                    orderDto.getCertificates().add(certificateDto);
////                }
////                orderService.createOrderInUser((long) generateInt(), orderDto);
////                count++;
////            }
////        }
////        br.close();
////        return true;
////    }
//
//
//    private int generateInt() {
//        return 1 + (int) (Math.random() * 1000);
//    }
//
//    public int generateSmall() {
//        return 1 + (int) (Math.random() * 3);
//    }
//
//}
