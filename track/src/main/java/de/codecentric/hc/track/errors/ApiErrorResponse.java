package de.codecentric.hc.track.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Response returned when problems encounter while performing an operation.
 *
 * @see <a href="https://jsonapi.org/format/#error-objects</a>
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class ApiErrorResponse {

    private List<ApiError> errors;
}
