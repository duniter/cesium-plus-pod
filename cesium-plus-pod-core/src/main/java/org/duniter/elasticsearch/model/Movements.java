package org.duniter.elasticsearch.model;

/*-
 * #%L
 * Duniter4j :: Core Client API
 * %%
 * Copyright (C) 2014 - 2017 EIS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.google.common.base.Preconditions;
import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.duniter.core.client.model.bma.BlockchainBlocks.*;

/**
 * Created by blavenie on 26/04/17.
 */
public final class Movements {

    private Movements() {
        // helper class
    }

    public static Stream<Movement> stream(final BlockchainBlock block) {
        Preconditions.checkNotNull(block);

        // No Tx
        if (CollectionUtils.isEmpty(block.getTransactions())) {
            return Stream.empty();
        }

        return Arrays.stream(block.getTransactions())
                .flatMap(tx -> Movements.streamFromTx(block, tx));
    }

    public static List<Movement> getMovements(final BlockchainBlock block) {
        return stream(block).collect(Collectors.toList());
    }


    /* -- Internal methods -- */

    private static Stream<Movement> streamFromTx(final BlockchainBlock block, final BlockchainBlock.Transaction tx) {

        final List<TxInput> inputs = getTxInputs(tx);
        final List<TxOutput> outputs = getTxOutputs(tx);
        final Set<String> recipients = getTxRecipients(outputs);

        final long totalAmount = inputs.stream().mapToLong(input -> powBase(input.amount, input.unitbase)).sum();

        return Arrays.stream(tx.getIssuers())
                .flatMap(issuer -> {
                    long issuerInputsAmount = getTxInputAmountByIssuer(inputs, issuer);
                    double issuerInputRatio = issuerInputsAmount / totalAmount;

                    return recipients.stream()
                            // Compute the recipient amount
                            .map(recipient -> {
                                Double recipientAmount = getTxOutputAmountByIssuerAndRecipient(outputs, issuer, recipient) * issuerInputRatio;
                                return Movement.newBuilder(block)
                                    .setAmount(recipientAmount.longValue(), 0/*unitbase*/)
                                    .setIssuer(issuer)
                                    .setRecipient(recipient)
                                    .setVersion(tx.getVersion())
                                    .setComment(tx.getComment())
                                    .build();
                            })
                            // Exclude movements to itself (e.g. changes)
                            .filter(movement -> movement.getAmount() != 0);
                });
    }

}
