package sa.gov.moe.etraining.module.registration.view;

import android.text.InputType;
import android.view.View;

import sa.gov.moe.etraining.module.registration.model.RegistrationFormField;

/**
 * Created by rohan on 2/11/15.
 */
class RegistrationTextView extends RegistrationEditTextView {

    public RegistrationTextView(RegistrationFormField field, View view) {
        super(field, view);
        mTextInputEditText.setInputType(InputType.TYPE_CLASS_TEXT);
    }
}
