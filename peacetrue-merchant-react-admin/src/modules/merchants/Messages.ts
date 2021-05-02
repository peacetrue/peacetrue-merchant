import {UserMessages} from "peacetrue-user";

export const MerchantMessages = {
  resources: {
    merchants: {
      name: '商家',
      fields: {
        ...UserMessages.resources.users.fields
      }
    },
  }
}

export default MerchantMessages;
