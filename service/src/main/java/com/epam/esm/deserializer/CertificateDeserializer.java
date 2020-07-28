package com.epam.esm.deserializer;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.exception.ValidationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Slf4j

public class CertificateDeserializer extends JsonDeserializer<CertificateDto> {
    private Map<String, String> errors = new HashMap<>();

    @Override
    public CertificateDto deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String name = node.get("name").textValue();
        String description = node.get("description").textValue();
        String price = node.get("price").asText();
        String duration = node.get("duration").asText();

        CertificateDto certificateDto = new CertificateDto();
        certificateDto.setName(validateName(name));
        certificateDto.setDescription(validateDescription(description));
        certificateDto.setPrice(validatePrice(price));
        certificateDto.setDuration(validateDuration(duration));

        if (!errors.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            errors.forEach((k, v) -> {
                builder.append("Field ").append(k).append(v).append("  ");
            });
            errors.clear();
            certificateDto = null;
            log.error(builder.toString());
            throw new ValidationException(builder.toString());
        }

        return certificateDto;
    }


    public String validateName(String name) {
        boolean matches = name.matches("([А-Яа-яa-zA-Z0-9 .!&?#,;$]){2,256}");
        if (name == null || !matches) {
            errors.put("name:", "Name must be between 2 and 256 characters.");
        }
        return name;
    }

    public String validateDescription(String description) {
        boolean matches = description.matches("[А-Яа-яa-zA-Z0-9 .!&?#,;$]{0,999}");
        if (description == null || !matches) {
            errors.put("description:", "Description must be between 2 and 999 characters.");
        }
        return description;
    }

    public BigDecimal validatePrice(String priceString) {
        BigDecimal num = new BigDecimal(0);

        try {
            num = new BigDecimal(priceString.trim());
            num = num.setScale(2, RoundingMode.HALF_EVEN);
            if (num.compareTo(new BigDecimal(0.00)) < 0) {
                throw new NumberFormatException();
            }
            if (num.compareTo(new BigDecimal(1000000000000.00)) > 0) {
                throw new NumberFormatException();
            }
            return num;
        } catch (NumberFormatException nfe) {
            errors.put("price:", "String '" + priceString + "' to decimal converting exception. Price should be 0.00 - 1000000000000.00.");
        }

        return num;
    }

    public int validateDuration(String durationString) {
        int num = 0;
        try {
            num = Integer.parseInt(durationString.trim());
            if (num < 0 || num > 100000) {
                throw new NumberFormatException();
            }
            return num;
        } catch (NumberFormatException nfe) {
            errors.put("duration:", "String '" + durationString + "' to integer converting exception. Range 0 - 100000.");
        }
        return num;
    }

}
