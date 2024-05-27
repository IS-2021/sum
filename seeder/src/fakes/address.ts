import { getGeo } from "../lib/api/geo/geo";

export async function getAddress(address: string) {
  const autocompletes = await getGeo().getGeoAutocomplete({
    query: address,
    sessionToken: crypto.randomUUID(),
  });

  if (!autocompletes.data[0]) {
    throw new Error(`No address found that matched ${address}`);
  }

  const placeId = autocompletes.data[0].placeId;

  const res = await getGeo().getGeoPlaces({
    placeId,
  });

  return res.data;
}
