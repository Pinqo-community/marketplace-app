package com.marketplace.validation;

/**
 * Marker interface used for validation during the creation of entities or DTOs.
 * This interface is used to specify that a method or class should be validated
 * during the creation of entities or DTOs.
 *
 * For example, this interface can be used in annotations like
 *  * { @Null(groups = OnCreate.class)} or { @NotNull(groups = OnCreate.class)}
 *  * to differentiate between creation and update operations.
 */
public interface OnCreate {
}
