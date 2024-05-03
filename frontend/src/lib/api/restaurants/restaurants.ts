/**
 * Generated by orval v6.27.1 🍺
 * Do not edit manually.
 * Sumatywny
 * OpenAPI spec version: 1.0.0
 */
import { useMutation, useQuery } from '@tanstack/vue-query';
import type {
  MutationFunction,
  QueryFunction,
  QueryKey,
  UseMutationOptions,
  UseMutationReturnType,
  UseQueryOptions,
  UseQueryReturnType,
} from '@tanstack/vue-query';
import * as axios from 'axios';
import type { AxiosError, AxiosRequestConfig, AxiosResponse } from 'axios';
import { computed, unref } from 'vue';
import type { MaybeRef } from 'vue';
import type {
  GetRestaurantsParams,
  NotFound404Response,
  RestaurantDTO,
  RestaurantInputDTO,
  Uuid,
  ValidationFailed422Response,
} from '../../api-model';

export const getRestaurants = (
  params?: MaybeRef<GetRestaurantsParams>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<RestaurantDTO[]>> => {
  params = unref(params);
  return axios.default.get(`http://localhost:9090/restaurants`, {
    ...options,
    params: { ...unref(params), ...options?.params },
  });
};

export const getGetRestaurantsQueryKey = (params?: MaybeRef<GetRestaurantsParams>) => {
  return ['http:', 'localhost:9090', 'restaurants', ...(params ? [params] : [])] as const;
};

export const getGetRestaurantsQueryOptions = <
  TData = Awaited<ReturnType<typeof getRestaurants>>,
  TError = AxiosError<unknown>,
>(
  params?: MaybeRef<GetRestaurantsParams>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getRestaurants>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
) => {
  const { query: queryOptions, axios: axiosOptions } = options ?? {};

  const queryKey = getGetRestaurantsQueryKey(params);

  const queryFn: QueryFunction<Awaited<ReturnType<typeof getRestaurants>>> = ({ signal }) =>
    getRestaurants(params, { signal, ...axiosOptions });

  return { queryKey, queryFn, ...queryOptions } as UseQueryOptions<
    Awaited<ReturnType<typeof getRestaurants>>,
    TError,
    TData
  >;
};

export type GetRestaurantsQueryResult = NonNullable<Awaited<ReturnType<typeof getRestaurants>>>;
export type GetRestaurantsQueryError = AxiosError<unknown>;

export const useGetRestaurants = <
  TData = Awaited<ReturnType<typeof getRestaurants>>,
  TError = AxiosError<unknown>,
>(
  params?: MaybeRef<GetRestaurantsParams>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getRestaurants>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
): UseQueryReturnType<TData, TError> & { queryKey: QueryKey } => {
  const queryOptions = getGetRestaurantsQueryOptions(params, options);

  const query = useQuery(queryOptions) as UseQueryReturnType<TData, TError> & {
    queryKey: QueryKey;
  };

  query.queryKey = unref(queryOptions).queryKey as QueryKey;

  return query;
};

export const postRestaurants = (
  restaurantInputDTO: MaybeRef<RestaurantInputDTO>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<RestaurantDTO>> => {
  restaurantInputDTO = unref(restaurantInputDTO);
  return axios.default.post(`http://localhost:9090/restaurants`, restaurantInputDTO, options);
};

export const getPostRestaurantsMutationOptions = <
  TError = AxiosError<ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof postRestaurants>>,
    TError,
    { data: RestaurantInputDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof postRestaurants>>,
  TError,
  { data: RestaurantInputDTO },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof postRestaurants>>,
    { data: RestaurantInputDTO }
  > = (props) => {
    const { data } = props ?? {};

    return postRestaurants(data, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type PostRestaurantsMutationResult = NonNullable<
  Awaited<ReturnType<typeof postRestaurants>>
>;
export type PostRestaurantsMutationBody = RestaurantInputDTO;
export type PostRestaurantsMutationError = AxiosError<ValidationFailed422Response>;

export const usePostRestaurants = <
  TError = AxiosError<ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof postRestaurants>>,
    TError,
    { data: RestaurantInputDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof postRestaurants>>,
  TError,
  { data: RestaurantInputDTO },
  TContext
> => {
  const mutationOptions = getPostRestaurantsMutationOptions(options);

  return useMutation(mutationOptions);
};
export const getRestaurantsId = (
  id: MaybeRef<Uuid>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<RestaurantDTO>> => {
  id = unref(id);
  return axios.default.get(`http://localhost:9090/restaurants/${id}`, options);
};

export const getGetRestaurantsIdQueryKey = (id: MaybeRef<Uuid>) => {
  return ['http:', 'localhost:9090', 'restaurants', id] as const;
};

export const getGetRestaurantsIdQueryOptions = <
  TData = Awaited<ReturnType<typeof getRestaurantsId>>,
  TError = AxiosError<NotFound404Response>,
>(
  id: MaybeRef<Uuid>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getRestaurantsId>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
) => {
  const { query: queryOptions, axios: axiosOptions } = options ?? {};

  const queryKey = getGetRestaurantsIdQueryKey(id);

  const queryFn: QueryFunction<Awaited<ReturnType<typeof getRestaurantsId>>> = ({ signal }) =>
    getRestaurantsId(id, { signal, ...axiosOptions });

  return {
    queryKey,
    queryFn,
    enabled: computed(() => !!unref(id)),
    ...queryOptions,
  } as UseQueryOptions<Awaited<ReturnType<typeof getRestaurantsId>>, TError, TData>;
};

export type GetRestaurantsIdQueryResult = NonNullable<Awaited<ReturnType<typeof getRestaurantsId>>>;
export type GetRestaurantsIdQueryError = AxiosError<NotFound404Response>;

export const useGetRestaurantsId = <
  TData = Awaited<ReturnType<typeof getRestaurantsId>>,
  TError = AxiosError<NotFound404Response>,
>(
  id: MaybeRef<Uuid>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getRestaurantsId>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
): UseQueryReturnType<TData, TError> & { queryKey: QueryKey } => {
  const queryOptions = getGetRestaurantsIdQueryOptions(id, options);

  const query = useQuery(queryOptions) as UseQueryReturnType<TData, TError> & {
    queryKey: QueryKey;
  };

  query.queryKey = unref(queryOptions).queryKey as QueryKey;

  return query;
};

export const putRestaurantsId = (
  id: MaybeRef<Uuid>,
  restaurantDTO: MaybeRef<RestaurantDTO>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<RestaurantDTO>> => {
  id = unref(id);
  restaurantDTO = unref(restaurantDTO);
  return axios.default.put(`http://localhost:9090/restaurants/${id}`, restaurantDTO, options);
};

export const getPutRestaurantsIdMutationOptions = <
  TError = AxiosError<NotFound404Response | ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof putRestaurantsId>>,
    TError,
    { id: Uuid; data: RestaurantDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof putRestaurantsId>>,
  TError,
  { id: Uuid; data: RestaurantDTO },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof putRestaurantsId>>,
    { id: Uuid; data: RestaurantDTO }
  > = (props) => {
    const { id, data } = props ?? {};

    return putRestaurantsId(id, data, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type PutRestaurantsIdMutationResult = NonNullable<
  Awaited<ReturnType<typeof putRestaurantsId>>
>;
export type PutRestaurantsIdMutationBody = RestaurantDTO;
export type PutRestaurantsIdMutationError = AxiosError<
  NotFound404Response | ValidationFailed422Response
>;

export const usePutRestaurantsId = <
  TError = AxiosError<NotFound404Response | ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof putRestaurantsId>>,
    TError,
    { id: Uuid; data: RestaurantDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof putRestaurantsId>>,
  TError,
  { id: Uuid; data: RestaurantDTO },
  TContext
> => {
  const mutationOptions = getPutRestaurantsIdMutationOptions(options);

  return useMutation(mutationOptions);
};
export const deleteRestaurantsId = (
  id: MaybeRef<Uuid>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<void>> => {
  id = unref(id);
  return axios.default.delete(`http://localhost:9090/restaurants/${id}`, options);
};

export const getDeleteRestaurantsIdMutationOptions = <
  TError = AxiosError<NotFound404Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof deleteRestaurantsId>>,
    TError,
    { id: Uuid },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof deleteRestaurantsId>>,
  TError,
  { id: Uuid },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof deleteRestaurantsId>>,
    { id: Uuid }
  > = (props) => {
    const { id } = props ?? {};

    return deleteRestaurantsId(id, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type DeleteRestaurantsIdMutationResult = NonNullable<
  Awaited<ReturnType<typeof deleteRestaurantsId>>
>;

export type DeleteRestaurantsIdMutationError = AxiosError<NotFound404Response>;

export const useDeleteRestaurantsId = <
  TError = AxiosError<NotFound404Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof deleteRestaurantsId>>,
    TError,
    { id: Uuid },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof deleteRestaurantsId>>,
  TError,
  { id: Uuid },
  TContext
> => {
  const mutationOptions = getDeleteRestaurantsIdMutationOptions(options);

  return useMutation(mutationOptions);
};
export const postRestaurantsImagesId = (
  id: MaybeRef<Uuid>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<void>> => {
  id = unref(id);
  return axios.default.post(`http://localhost:9090/restaurants/images/${id}`, undefined, options);
};

export const getPostRestaurantsImagesIdMutationOptions = <
  TError = AxiosError<void>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof postRestaurantsImagesId>>,
    TError,
    { id: Uuid },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof postRestaurantsImagesId>>,
  TError,
  { id: Uuid },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof postRestaurantsImagesId>>,
    { id: Uuid }
  > = (props) => {
    const { id } = props ?? {};

    return postRestaurantsImagesId(id, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type PostRestaurantsImagesIdMutationResult = NonNullable<
  Awaited<ReturnType<typeof postRestaurantsImagesId>>
>;

export type PostRestaurantsImagesIdMutationError = AxiosError<void>;

export const usePostRestaurantsImagesId = <
  TError = AxiosError<void>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof postRestaurantsImagesId>>,
    TError,
    { id: Uuid },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof postRestaurantsImagesId>>,
  TError,
  { id: Uuid },
  TContext
> => {
  const mutationOptions = getPostRestaurantsImagesIdMutationOptions(options);

  return useMutation(mutationOptions);
};
