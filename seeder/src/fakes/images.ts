import axios from "axios";
import { logEvent } from "../lib/logger";

export async function getRandomFoodImage() {
  const url = "https://source.unsplash.com/random/1920x1080/?food";

  const res = await fetch(url);
  const imgBlob = await res.blob();

  const date = new Date();
  const filename = date.toISOString().replace(/:/g, "-") + ".jpg";

  return new File([imgBlob], filename);
}

export async function addRandomImageToRestaurant(restaurantId: string) {
  const randomImageBlob = await getRandomFoodImage();
  const formData = new FormData();
  formData.append("image", randomImageBlob);

  const res = await axios.post(
    `http://localhost:9090/restaurants/images/${restaurantId}`,
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    },
  );

  if (res.status === 200) {
    logEvent({
      msg: `Added random image to restaurant ${restaurantId}`,
    });
  }
}
