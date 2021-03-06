package org.svenson.tokenize;

import java.math.BigDecimal;

/**
 * Defines all possible token types and the possible content for each.
 * @author fforw at gmx dot de
 *
 */
public enum TokenType
{
    BRACE_OPEN("{"),
    BRACE_CLOSE("}"),
    COLON(":"),
    BRACKET_OPEN("["),
    BRACKET_CLOSE("]"),
    COMMA(","),
    STRING(String.class),
    INTEGER(Long.class),
    DECIMAL(BigDecimal.class),
    TRUE(Boolean.TRUE),
    FALSE(Boolean.FALSE),
    NULL(null),
    END(null);

    private Object validContent;

    /**
     * Constructs a new TokenType instance with the given valid content.
     * <p>
     * if the valid content is a class, only values with that type are allowed. if
     * the valid content parameter is not a class, the content of this TokenType must
     * be the given content parameter.
     * @param validContent Defines what content values are allowed for this token type.
     */
    private TokenType(Object validContent)
    {
        this.validContent = validContent;
    }

    /**
     * Throws an {@link IllegalArgumentException} if the given value does not match
     * the requirements of this token type.
     * @param value
     * @throws IllegalArgumentException
     */
    public void checkValue(Object value) throws IllegalArgumentException
    {
        if (isClassRestricted())
        {
            Class cls = (Class)validContent;
            if ( !cls.isAssignableFrom(value.getClass()))
            {
                throw new IllegalArgumentException("Values for "+this.name()+" must be a "+cls);
            }
        }
        else
        {
            if (validContent == null)
            {
                if (value != null)
                {
                    throw new IllegalArgumentException("Only null values allowed for "+this.name());
                }
            }
            else
            {
                if (!validContent.equals(value))
                {
                    throw new IllegalArgumentException("Value for "+this.name()+" must be "+validContent);
                }
            }
        }
    }

    /**
     * Returns <code>true</code> if the valid content for this token type is restricted to instances of a class.
     * @return
     */
    public boolean isClassRestricted()
    {
        return validContent instanceof Class;
    }

    /**
     * Returns the valid content for this token type.
     * @return
     */
    public Object getValidContent()
    {
        return validContent;
    }

    /**
     * Returns true if the token type is one of
     *
     * <ul>
     * <li> {@link TokenType#TRUE}</li>
     * <li> {@link TokenType#FALSE}</li>
     * <li> {@link TokenType#NULL}</li>
     * <li> {@link TokenType#STRING}</li>
     * <li> {@link TokenType#INTEGER}</li>
     * <li> {@link TokenType#DECIMAL}</li>
     * </ul>
     * @return
     */
    public boolean isPrimitive()
    {
        switch(this)
        {
            case STRING:
            case TRUE:
            case FALSE:
            case NULL:
            case INTEGER:
            case DECIMAL:
                return true;
            default:
                return false;
        }
    }
}
