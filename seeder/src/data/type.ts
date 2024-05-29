import { RestaurantInputDTO } from "../lib/api-model";

export type RestaurantData = Omit<RestaurantInputDTO, "addressInputDTO" | "userId"> & {
  address: string;
  imageUrl: string;
};
