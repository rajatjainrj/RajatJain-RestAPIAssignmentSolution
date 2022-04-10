package xyz.rajatjain.employeemanagement.utils;

import org.springframework.http.ResponseEntity;
import xyz.rajatjain.employeemanagement.dtos.ResponseObj;
import xyz.rajatjain.employeemanagement.dtos.ResponseObjPagination;
import xyz.rajatjain.employeemanagement.dtos.ResponseStatus;

/**
 * @author rajatjain on - 10-04-2022
 * @project employeeManagement
 */
public class ResponseUtil {

    public static ResponseEntity<ResponseObj> getSuccessResponse(Object data, String message) {
        return ResponseEntity.ok().body(
                ResponseObj.builder().
                        status(ResponseStatus.SUCCESS).
                        data(data).
                        message(message).
                        build());
    }

    public static ResponseEntity<ResponseObjPagination> getSuccessResponsePagination(Object data, String message,
                                                                                     Integer totalPages,
                                                                                     Long totalNoOfElements) {
        return ResponseEntity.ok().body(
                ResponseObjPagination.builder().
                        status(ResponseStatus.SUCCESS).
                        data(data).
                        message(message).
                        totalNoOfElements(totalNoOfElements).
                        totalPages(totalPages).
                        build());
    }

    public static ResponseEntity<ResponseObj> getFailureResponse(Object data, String message) {
        return ResponseEntity.badRequest().body(
                ResponseObj.builder().
                        status(ResponseStatus.FAILURE).
                        data(data).
                        message(message).
                        build());
    }

    public static ResponseEntity<ResponseObj> getFailureResponse(String message) {
        return getFailureResponse(null, message);
    }

    public static ResponseEntity<ResponseObj> getErrorResponse(Exception e) {
        return ResponseEntity.internalServerError().body(
                ResponseObj.builder().
                        status(ResponseStatus.ERROR).
                        data(e.getMessage()).
                        message("System error has occurred").
                        build());
    }

    public static ResponseEntity<ResponseObjPagination> getErrorResponsePagination(Exception e) {
        return ResponseEntity.internalServerError().body(
                ResponseObjPagination.builder().
                        status(ResponseStatus.ERROR).
                        data(e.getMessage()).
                        message("System error has occurred").
                        build());
    }
}
