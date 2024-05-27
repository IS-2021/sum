import { RestaurantInputDTO } from "./lib/api-model";

type RestaurantData = Omit<RestaurantInputDTO, "addressInputDTO" | "userId"> & {
  address: string;
  imageUrl: string;
};

export const restaurants: RestaurantData[] = [
  {
    name: "Whiskey in the Jar",
    address: "ul. Drewnowska 58 B Manufaktura, Lodz 91-002 Poland",
    hours: {
      sunday: ["11:00", "22:00"],
      monday: ["11:00", "23:00"],
      tuesday: ["11:00", "23:00"],
      wednesday: ["11:00", "23:00"],
      thursday: ["11:00", "23:00"],
      friday: ["11:00", "23:00"],
      saturday: ["11:00", "23:00"],
    },
    phoneNumber: "516 136 876",
    imageUrl: "https://media-cdn.tripadvisor.com/media/photo-o/11/4d/f1/32/mr-t-t-bone-550g.jpg",
  },
];
