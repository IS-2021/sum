/**
 * Generated by orval v6.27.1 🍺
 * Do not edit manually.
 * Sumatywny
 * OpenAPI spec version: 1.0.0
 */
import { useMutation } from '@tanstack/vue-query';
import type {
  MutationFunction,
  UseMutationOptions,
  UseMutationReturnType,
} from '@tanstack/vue-query';
import * as axios from 'axios';
import type { AxiosError, AxiosRequestConfig, AxiosResponse } from 'axios';
import { unref } from 'vue';
import type { MaybeRef } from 'vue';

export const postRestaurantsImagesId = (
  id: MaybeRef<unknown>,
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
    { id: unknown },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof postRestaurantsImagesId>>,
  TError,
  { id: unknown },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof postRestaurantsImagesId>>,
    { id: unknown }
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
    { id: unknown },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof postRestaurantsImagesId>>,
  TError,
  { id: unknown },
  TContext
> => {
  const mutationOptions = getPostRestaurantsImagesIdMutationOptions(options);

  return useMutation(mutationOptions);
};
