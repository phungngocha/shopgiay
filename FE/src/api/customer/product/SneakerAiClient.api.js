import { requestCustomer } from "../../../helper/request";

export class SneakerAiClientApi {
  static consultSneaker = (need) => {
    return requestCustomer({
      method: "POST",
      url: "/client/ai/consult-sneaker",
      data: {
        need: need,
      },
    });
  };
}
