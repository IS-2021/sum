import { getCities } from "src/lib/api/cities/cities";

export async function getCityId(name: string) {
  const { data } = await getCities().getCities();
  return data.find((city) => city.name === name)?.cityId;
}
