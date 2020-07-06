package com.epam.esm.service.deserializer;

import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.JsonDeserializationException;
import com.epam.esm.service.exception.NotFoundException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
//@Component
public class CertificateDeserializer extends StdDeserializer<CertificateDto> {
    private CertificateService<CertificateDto, Long> certificateService;
    private TagDeserializer tagDeserializer;

    public CertificateDeserializer() {
        this(null);
    }

    public CertificateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Autowired
    public void setTagService(CertificateService<CertificateDto, Long> certificateService) {
        this.certificateService = certificateService;
    }

    @Autowired
    public void setTagDeserializer(TagDeserializer tagDeserializer) {
        this.tagDeserializer = tagDeserializer;
    }


    @Override
    public CertificateDto deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        return deserializeCertificateNode(node);
    }

    public CertificateDto deserializeCertificateNode(JsonNode node) {
        List<String> errorList = new ArrayList<>();
        CertificateDto certificateDto = new CertificateDto();

        Long id = null;
        try {
            id = node.get("id").longValue();
            if (id < 1) {
                throw new JsonDeserializationException("id must be more then 0. ");
            }
            Optional<CertificateDto> certificateDtoOptional = certificateService.get(id);
            if (certificateDtoOptional.isPresent()) {
                certificateDto = certificateDtoOptional.get();
                certificateDto.getTags().clear();
            } else {
                throw new NotFoundException(id);
            }
        } catch (Exception e) {
            if (e.getClass().equals(NotFoundException.class)) {
                throw new NotFoundException(id);
            }
//            errorList.add("id exception:" + e.getMessage());
            log.warn("Certificate exception, id:" + e.getMessage());
        }


        String name = null;
        try {
            if (node.get("name") == null) {
                throw new JsonDeserializationException("name must be between 2 and 256 characters. ");
            }
            name = node.get("name").asText();

            if (name.length() < 2 || name.length() > 256) {
                throw new JsonDeserializationException("name must be between 2 and 256 characters. ");
            }
            certificateDto.setName(name);
        } catch (Exception e) {
            errorList.add("name exception:" + e.getMessage());
        }


        String description = null;
        try {
            if (node.get("description") == null) {
                throw new JsonDeserializationException("description must not be null. ");
            }
            description = node.get("description").asText();
            if (description.length() > 999) {
                throw new JsonDeserializationException("Description must be null or between 0 and 999 characters. ");
            }
            certificateDto.setDescription(description);
        } catch (Exception e) {
            errorList.add("description exception:" + e.getMessage());
        }


        BigDecimal price = null;
        try {
            if (node.get("price") == null) {
                throw new JsonDeserializationException("Price should be 0.00 - 1000000000000.00. ");
            }
            price = node.get("price").decimalValue();
            price = price.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            if ((price.compareTo(new BigDecimal("1000000000000")) > 0) ||
                    (price.compareTo(new BigDecimal(0.00)) < 0)) {
                throw new JsonDeserializationException("Price should be 0.00 - 1000000000000.00. ");
            }
            certificateDto.setPrice(price);
        } catch (Exception e) {
            errorList.add("price exception:" + e.getMessage());
        }


        int duration = -1;
        try {
            if (node.get("duration") == null) {
                throw new JsonDeserializationException("duration should be 0.00 - 100000. ");
            }
            String durationStr = node.get("duration").asText();
            if (!durationStr.matches("[0-9]{1,6}")) {
                throw new JsonDeserializationException("format exception duration should be 0.00 - 100000. ");
            }
            duration = node.get("duration").intValue();
            if (duration < 0 || duration > 100000) {
                throw new JsonDeserializationException("Duration should be 0 - 100000. ");
            }
            certificateDto.setDuration(duration);
        } catch (Exception e) {
            errorList.add("duration exception:" + e.getMessage());
        }


        if (node.get("tags") != null) {
            Iterator<JsonNode> tagsIterator = node.get("tags").elements();
            Set<TagDto> tagDtos = new HashSet<>();

            try {
                while (tagsIterator.hasNext()) {
                    JsonNode tagNode = tagsIterator.next();
                    TagDto tagDto = tagDeserializer.deserializeTagNode(tagNode);
                    tagDtos.add(tagDto);
                }
                certificateDto.setTags(tagDtos);
            } catch (Exception e) {
                errorList.add(e.getMessage());
            }
        }

        if (!errorList.isEmpty()) {
            String result = errorList.stream().reduce("", (one, two) -> one + two);
            log.warn(result);
            throw new JsonDeserializationException(result);
        }


        return certificateDto;
    }
}
