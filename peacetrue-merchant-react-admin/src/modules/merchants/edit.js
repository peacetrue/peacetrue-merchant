import React from 'react';
import {Edit, maxLength, minLength, PasswordInput, regex, required, SimpleForm, TextInput} from 'react-admin';

export const MerchantEdit = (props) => {
    console.info('MerchantEdit:', props);
    let validate = [required(), minLength(6), maxLength(32), regex(/^[0-9a-zA-Z-.]+$/)];
    return (
        <Edit {...props} title={`${props.options.label}#${props.id}`}>
            <SimpleForm>
                <TextInput label={'商家名'} source="username" validate={validate}/>
                <PasswordInput label={'密码'} source="password" validate={validate}/>
            </SimpleForm>
        </Edit>
    );
};
