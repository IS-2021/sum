import axios from "axios";
import { logEvent } from "../lib/logger";

export async function getImage(url: string) {
  const res = await fetch(url);
  const imgBlob = await res.blob();

  const date = new Date();
  const filename = date.toISOString().replace(/:/g, "-") + ".jpg";

  return new File([imgBlob], filename);
}

export async function addRandomImageToRestaurant(restaurantId: string, imageUrl: string) {
  const randomImageBlob = await getImage(imageUrl);
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
