import { RestaurantData } from "./data/type";
import { restaurantsTorun } from "./data/torun";
import { restaurantsLodz } from "./data/lodz";
import { restaurantsWarszawa } from "./data/warszawa";

const allRestaurants: RestaurantData[] = [
  ...restaurantsTorun,
  ...restaurantsLodz,
  ...restaurantsWarszawa,
];

const singleRestauarnt: RestaurantData[] = [
  {
    name: "Forno Nero Pizza Napoletana",
    address: "Piotrkowska 79 By Car Kościuszki 22, Lodz 90-423 Poland",
    hours: {
      sunday: ["12:00", "22:00"],
      monday: ["12:00", "22:00"],
      tuesday: ["12:00", "22:00"],
      wednesday: ["12:00", "22:00"],
      thursday: ["12:00", "22:00"],
      friday: ["12:00", "00:00"],
      saturday: ["12:00", "00:00"],
    },
    phoneNumber: "574 781 881",
    imageUrl: "https://media-cdn.tripadvisor.com/media/photo-m/1280/13/ad/3c/32/etna.jpg",
  },
];

export const restaurants = allRestaurants;
// export const restaurants = singleRestauarnt;
