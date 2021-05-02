import * as React from 'react';
import {EditProps, PasswordInput, SimpleForm, TextField} from 'react-admin';
import {PeaceEdit} from "peacetrue-react-admin";
import {UserCreateModifyFields, UserRules} from "peacetrue-user";

export const MerchantEdit = (props: EditProps) => {
  console.info('MerchantEdit:', props);
  return (
    <PeaceEdit  {...props}>
      <SimpleForm>
        <TextField source="username"/>
        <PasswordInput source="password" validate={UserRules.password}/>
        {UserCreateModifyFields}
      </SimpleForm>
    </PeaceEdit>
  );
};
