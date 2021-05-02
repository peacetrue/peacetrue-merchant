import * as React from "react";
import {Resource} from "react-admin";

import MerchantIcon from '@material-ui/icons/People';
import {MerchantList} from './List';
import {MerchantCreate} from './Create';
import {MerchantEdit} from "./Edit";
import {MerchantShow} from './Show';
import {buildUserCreateFields, buildUserCreateModifyFields, buildUserModifyFields} from "peacetrue-user";

export {MerchantMessages} from "./Messages"

const resource = "merchants";
export const MerchantCreateFields = buildUserCreateFields(resource);
export const MerchantModifyFields = buildUserModifyFields(resource);
export const MerchantCreateModifyFields = buildUserCreateModifyFields(resource);
export const Merchant = {list: MerchantList, create: MerchantCreate, edit: MerchantEdit, show: MerchantShow};
export const MerchantResource = <Resource icon={MerchantIcon} name={resource} {...Merchant} />;
export default MerchantResource;
