package com.pokemoney.hadoop.hbase.dto.invitation;

import lombok.*;

/**
 * The DTOs for the invitation response.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InvitationResponseDto {
    /**
     * The success flag.
     */
    private Boolean success;
    /**
     * The message.
     */
    private String message;
}
