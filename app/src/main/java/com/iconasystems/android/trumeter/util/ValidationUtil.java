
package com.iconasystems.android.trumeter.util;

import java.util.Iterator;
import java.util.List;

public class ValidationUtil {

    public static void validate(Validation... args) {
        for (Validation validation : args) {
            validation.validate();
        }
    }

    public static void pruneInvalid(List<? extends Validation> validations) {
        Iterator<? extends Validation> itr = validations.iterator();
        while (itr.hasNext()) {
            try {
                Validation next = itr.next();
                next.validate();
            } catch (ValidationFailedException ex) {
                itr.remove();
            }
        }
    }
}
