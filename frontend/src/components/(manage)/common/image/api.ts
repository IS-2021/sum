import axios from 'axios';

export function uploadRestaurantImage(restaurantId: string, image: File) {
  const formData = new FormData();
  formData.append('image', image);

  return axios.post(`http://localhost:9090/restaurants/images/${restaurantId}`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}
