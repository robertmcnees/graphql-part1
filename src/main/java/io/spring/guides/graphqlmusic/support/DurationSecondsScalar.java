package io.spring.guides.graphqlmusic.support;

import java.math.BigInteger;
import java.time.Duration;

import graphql.language.IntValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

public final class DurationSecondsScalar {

    public static final GraphQLScalarType INSTANCE;

    private DurationSecondsScalar() {
    }

    static {
        Coercing<Duration, Long> coercing = new Coercing<Duration, Long>() {
            @Override
            public Long serialize(Object input) throws CoercingSerializeException {
                if (input instanceof Duration duration) {
                    return duration.toSeconds();
                } else if (input instanceof String aString) {
                    return Duration.ofSeconds(Long.parseLong(aString)).toSeconds();
                } else {
                    throw new CoercingSerializeException(
                            "Expected a 'Long' or 'java.time.Duration' but was ."
                    );
                }
            }

            @Override
            public Duration parseValue(Object input) throws CoercingParseValueException {
                if (input instanceof Duration duration) {
                    return duration;
                } else if (input instanceof String aString) {
                    return Duration.ofSeconds(Long.parseLong(aString));
                } else {
                    throw new CoercingParseValueException(
                            "Expected a 'Long' or 'java.time.Duration' but was ."
                    );
                }
            }

            @Override
            public Duration parseLiteral(Object input) throws CoercingParseLiteralException {
                if (input instanceof IntValue intValue) {
                    return Duration.ofSeconds(intValue.getValue().longValue());
                }
                throw new CoercingParseLiteralException(
                        "Expected AST type 'StringValue' but was ."
                );
            }

            @Override
            public Value<?> valueToLiteral(Object input) {
                Long aLong = serialize(input);
                return IntValue.newIntValue(BigInteger.valueOf(aLong)).build();
            }

        };

        INSTANCE = GraphQLScalarType.newScalar()
                .name("Duration")
                .description("A duration in seconds")
                .coercing(coercing)
                .build();
    }

}
