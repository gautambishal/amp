package org.dgfoundation.amp.onepager.validators;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.dgfoundation.amp.onepager.models.TranslationDecoratorModel;

import java.util.List;

/**
 * @author aartimon
 * @since 06/12/13
 */
public class TranslatableValidators implements IValidator<String> {
    private final List<IValidator<? super String>> validators;
    private final IModel<String> newModel;
    public IModel<String> originalModel;

    public TranslatableValidators(IModel<String> originalModel, IModel<String> newModel , List<IValidator<? super String>> validators) {
        this.originalModel = originalModel;
        this.newModel = newModel;
        this.validators = validators;
    }

    @Override
    public void validate(IValidatable<String> validatable) {
        //we need to wrap validatable to return the output of the current model
        TranslatableValidatableWrapper<String> v = new TranslatableValidatableWrapper<String>(validatable, newModel) {
            @Override
            protected String newGetValue() {
                if (originalModel.getObject() == null)
                    return "";
                return originalModel.getObject();
            }
        };

        for (IValidator validator: validators){
            validator.validate(v);
        }
    }

    public static void alter(IModel<String> origModel, FormComponent<String> component) {
        if (component.getModel() instanceof TranslationDecoratorModel){
            List<IValidator<? super String>> validators = component.getValidators();
            for (IValidator validator : validators){
                component.remove(validator);
            }
            TranslatableValidators tv = new TranslatableValidators(origModel, component.getModel(), validators);
            component.add(tv);
        }
    }

    public static void onError(AjaxRequestTarget target, FormComponent<?> formComponent, Component translationDecorator) {
        if (formComponent.getModel() instanceof TranslationDecoratorModel){
            TranslationDecoratorModel tdm = (TranslationDecoratorModel) formComponent.getModel();
            if (tdm.getLangModel().getObject() != null){
                //we need to switch the language tab to the current interface language
                //since the validation is only done for the current language
                tdm.getLangModel().setObject(null);
                formComponent.clearInput();
                if (translationDecorator != null)
                    target.add(translationDecorator);
            }
        }
    }

    public List<IValidator<? super String>> getValidators() {
        return validators;
    }
}
