import * as React from 'react';
import {Create, CreateProps, PasswordInput, SimpleForm, TextInput,} from 'react-admin';
import {UserRules} from "peacetrue-user";

export const MerchantCreate = (props: CreateProps) => {
  console.info('MerchantCreate:', props);
  return (
    <Create {...props}>
      <SimpleForm>
        <TextInput source="username" validate={UserRules.username}/>
        <PasswordInput source="password" validate={UserRules.password}/>
      </SimpleForm>
    </Create>
  );
};
