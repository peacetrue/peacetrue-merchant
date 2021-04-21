import React from "react";
import {Resource} from "react-admin";

import {MerchantList} from './list';
import {MerchantCreate} from './create';
import {MerchantEdit} from './edit';
import {MerchantShow} from './show';

export const Merchant = {list: MerchantList, create: MerchantCreate, edit: MerchantEdit, show: MerchantShow};
const MerchantResource = <Resource options={{label: '商家'}} name="users" {...Merchant} />;
export default MerchantResource;
