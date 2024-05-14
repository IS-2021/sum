import { getCities } from "src/lib/api/default/default";

export async function getCityId(name: string) {
  const { data } = await getCities();
  return data.find((city) => city.name === name)?.cityId;
}
