package com.auzcean.macolabackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * function: general delete request class
 */
@Data
public class DeleteRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
