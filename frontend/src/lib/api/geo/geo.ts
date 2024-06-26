/**
 * Generated by orval v6.27.1 🍺
 * Do not edit manually.
 * Sumatywny
 * OpenAPI spec version: 1.0.0
 */
import { useQuery } from '@tanstack/vue-query';
import type {
  QueryFunction,
  QueryKey,
  UseQueryOptions,
  UseQueryReturnType,
} from '@tanstack/vue-query';
import * as axios from 'axios';
import type { AxiosError, AxiosRequestConfig, AxiosResponse } from 'axios';
import { unref } from 'vue';
import type { MaybeRef } from 'vue';
import type {
  AddressDTO,
  AutocompleteDTO,
  GetGeoAutocompleteParams,
  GetGeoPlacesParams,
  GetGeoReverseGeocodeParams,
} from '../../api-model';

export const getGeoAutocomplete = (
  params: MaybeRef<GetGeoAutocompleteParams>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<AutocompleteDTO[]>> => {
  params = unref(params);
  return axios.default.get(`http://localhost:9090/geo/autocomplete`, {
    ...options,
    params: { ...unref(params), ...options?.params },
  });
};

export const getGetGeoAutocompleteQueryKey = (params: MaybeRef<GetGeoAutocompleteParams>) => {
  return ['http:', 'localhost:9090', 'geo', 'autocomplete', ...(params ? [params] : [])] as const;
};

export const getGetGeoAutocompleteQueryOptions = <
  TData = Awaited<ReturnType<typeof getGeoAutocomplete>>,
  TError = AxiosError<unknown>,
>(
  params: MaybeRef<GetGeoAutocompleteParams>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getGeoAutocomplete>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
) => {
  const { query: queryOptions, axios: axiosOptions } = options ?? {};

  const queryKey = getGetGeoAutocompleteQueryKey(params);

  const queryFn: QueryFunction<Awaited<ReturnType<typeof getGeoAutocomplete>>> = ({ signal }) =>
    getGeoAutocomplete(params, { signal, ...axiosOptions });

  return { queryKey, queryFn, ...queryOptions } as UseQueryOptions<
    Awaited<ReturnType<typeof getGeoAutocomplete>>,
    TError,
    TData
  >;
};

export type GetGeoAutocompleteQueryResult = NonNullable<
  Awaited<ReturnType<typeof getGeoAutocomplete>>
>;
export type GetGeoAutocompleteQueryError = AxiosError<unknown>;

export const useGetGeoAutocomplete = <
  TData = Awaited<ReturnType<typeof getGeoAutocomplete>>,
  TError = AxiosError<unknown>,
>(
  params: MaybeRef<GetGeoAutocompleteParams>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getGeoAutocomplete>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
): UseQueryReturnType<TData, TError> & { queryKey: QueryKey } => {
  const queryOptions = getGetGeoAutocompleteQueryOptions(params, options);

  const query = useQuery(queryOptions) as UseQueryReturnType<TData, TError> & {
    queryKey: QueryKey;
  };

  query.queryKey = unref(queryOptions).queryKey as QueryKey;

  return query;
};

export const getGeoPlaces = (
  params: MaybeRef<GetGeoPlacesParams>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<AddressDTO>> => {
  params = unref(params);
  return axios.default.get(`http://localhost:9090/geo/places`, {
    ...options,
    params: { ...unref(params), ...options?.params },
  });
};

export const getGetGeoPlacesQueryKey = (params: MaybeRef<GetGeoPlacesParams>) => {
  return ['http:', 'localhost:9090', 'geo', 'places', ...(params ? [params] : [])] as const;
};

export const getGetGeoPlacesQueryOptions = <
  TData = Awaited<ReturnType<typeof getGeoPlaces>>,
  TError = AxiosError<unknown>,
>(
  params: MaybeRef<GetGeoPlacesParams>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getGeoPlaces>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
) => {
  const { query: queryOptions, axios: axiosOptions } = options ?? {};

  const queryKey = getGetGeoPlacesQueryKey(params);

  const queryFn: QueryFunction<Awaited<ReturnType<typeof getGeoPlaces>>> = ({ signal }) =>
    getGeoPlaces(params, { signal, ...axiosOptions });

  return { queryKey, queryFn, ...queryOptions } as UseQueryOptions<
    Awaited<ReturnType<typeof getGeoPlaces>>,
    TError,
    TData
  >;
};

export type GetGeoPlacesQueryResult = NonNullable<Awaited<ReturnType<typeof getGeoPlaces>>>;
export type GetGeoPlacesQueryError = AxiosError<unknown>;

export const useGetGeoPlaces = <
  TData = Awaited<ReturnType<typeof getGeoPlaces>>,
  TError = AxiosError<unknown>,
>(
  params: MaybeRef<GetGeoPlacesParams>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getGeoPlaces>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
): UseQueryReturnType<TData, TError> & { queryKey: QueryKey } => {
  const queryOptions = getGetGeoPlacesQueryOptions(params, options);

  const query = useQuery(queryOptions) as UseQueryReturnType<TData, TError> & {
    queryKey: QueryKey;
  };

  query.queryKey = unref(queryOptions).queryKey as QueryKey;

  return query;
};

export const getGeoReverseGeocode = (
  params: MaybeRef<GetGeoReverseGeocodeParams>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<AddressDTO>> => {
  params = unref(params);
  return axios.default.get(`http://localhost:9090/geo/reverse-geocode`, {
    ...options,
    params: { ...unref(params), ...options?.params },
  });
};

export const getGetGeoReverseGeocodeQueryKey = (params: MaybeRef<GetGeoReverseGeocodeParams>) => {
  return [
    'http:',
    'localhost:9090',
    'geo',
    'reverse-geocode',
    ...(params ? [params] : []),
  ] as const;
};

export const getGetGeoReverseGeocodeQueryOptions = <
  TData = Awaited<ReturnType<typeof getGeoReverseGeocode>>,
  TError = AxiosError<unknown>,
>(
  params: MaybeRef<GetGeoReverseGeocodeParams>,
  options?: {
    query?: Partial<
      UseQueryOptions<Awaited<ReturnType<typeof getGeoReverseGeocode>>, TError, TData>
    >;
    axios?: AxiosRequestConfig;
  },
) => {
  const { query: queryOptions, axios: axiosOptions } = options ?? {};

  const queryKey = getGetGeoReverseGeocodeQueryKey(params);

  const queryFn: QueryFunction<Awaited<ReturnType<typeof getGeoReverseGeocode>>> = ({ signal }) =>
    getGeoReverseGeocode(params, { signal, ...axiosOptions });

  return { queryKey, queryFn, ...queryOptions } as UseQueryOptions<
    Awaited<ReturnType<typeof getGeoReverseGeocode>>,
    TError,
    TData
  >;
};

export type GetGeoReverseGeocodeQueryResult = NonNullable<
  Awaited<ReturnType<typeof getGeoReverseGeocode>>
>;
export type GetGeoReverseGeocodeQueryError = AxiosError<unknown>;

export const useGetGeoReverseGeocode = <
  TData = Awaited<ReturnType<typeof getGeoReverseGeocode>>,
  TError = AxiosError<unknown>,
>(
  params: MaybeRef<GetGeoReverseGeocodeParams>,
  options?: {
    query?: Partial<
      UseQueryOptions<Awaited<ReturnType<typeof getGeoReverseGeocode>>, TError, TData>
    >;
    axios?: AxiosRequestConfig;
  },
): UseQueryReturnType<TData, TError> & { queryKey: QueryKey } => {
  const queryOptions = getGetGeoReverseGeocodeQueryOptions(params, options);

  const query = useQuery(queryOptions) as UseQueryReturnType<TData, TError> & {
    queryKey: QueryKey;
  };

  query.queryKey = unref(queryOptions).queryKey as QueryKey;

  return query;
};
