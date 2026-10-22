package service.util;

import service.Exceptions.BadRequestException;

public class RequestValidator {

    public static void validateRequiredFields(String... fields) throws BadRequestException {
        for (String field : fields) {
            if (field == null || field.isBlank()) {
                throw new BadRequestException("Missing fields");
            }
        }
    }
}
