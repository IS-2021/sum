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
import type { BadRequest400Response, NotFound404Response, UserMeDTO, Uuid } from '../../api-model';

export const postUsersUserIdCityCityId = (
  userId: MaybeRef<Uuid>,
  cityId: MaybeRef<Uuid>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<UserMeDTO>> => {
  userId = unref(userId);
  cityId = unref(cityId);
  return axios.default.post(
    `http://localhost:9090/users/${userId}/city/${cityId}`,
    undefined,
    options,
  );
};

export const getPostUsersUserIdCityCityIdMutationOptions = <
  TError = AxiosError<BadRequest400Response | NotFound404Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof postUsersUserIdCityCityId>>,
    TError,
    { userId: Uuid; cityId: Uuid },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof postUsersUserIdCityCityId>>,
  TError,
  { userId: Uuid; cityId: Uuid },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof postUsersUserIdCityCityId>>,
    { userId: Uuid; cityId: Uuid }
  > = (props) => {
    const { userId, cityId } = props ?? {};

    return postUsersUserIdCityCityId(userId, cityId, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type PostUsersUserIdCityCityIdMutationResult = NonNullable<
  Awaited<ReturnType<typeof postUsersUserIdCityCityId>>
>;

export type PostUsersUserIdCityCityIdMutationError = AxiosError<
  BadRequest400Response | NotFound404Response
>;

export const usePostUsersUserIdCityCityId = <
  TError = AxiosError<BadRequest400Response | NotFound404Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof postUsersUserIdCityCityId>>,
    TError,
    { userId: Uuid; cityId: Uuid },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof postUsersUserIdCityCityId>>,
  TError,
  { userId: Uuid; cityId: Uuid },
  TContext
> => {
  const mutationOptions = getPostUsersUserIdCityCityIdMutationOptions(options);

  return useMutation(mutationOptions);
};
