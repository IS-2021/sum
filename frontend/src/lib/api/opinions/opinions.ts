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
import type {
  NotFound404Response,
  OpinionDTO,
  OpinionInputDTO,
  Uuid,
  ValidationFailed422Response,
} from '../../api-model';

export const postOpinions = (
  opinionInputDTO: MaybeRef<OpinionInputDTO>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<OpinionDTO>> => {
  opinionInputDTO = unref(opinionInputDTO);
  return axios.default.post(`http://localhost:9090/opinions`, opinionInputDTO, options);
};

export const getPostOpinionsMutationOptions = <
  TError = AxiosError<ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof postOpinions>>,
    TError,
    { data: OpinionInputDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof postOpinions>>,
  TError,
  { data: OpinionInputDTO },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof postOpinions>>,
    { data: OpinionInputDTO }
  > = (props) => {
    const { data } = props ?? {};

    return postOpinions(data, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type PostOpinionsMutationResult = NonNullable<Awaited<ReturnType<typeof postOpinions>>>;
export type PostOpinionsMutationBody = OpinionInputDTO;
export type PostOpinionsMutationError = AxiosError<ValidationFailed422Response>;

export const usePostOpinions = <
  TError = AxiosError<ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof postOpinions>>,
    TError,
    { data: OpinionInputDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof postOpinions>>,
  TError,
  { data: OpinionInputDTO },
  TContext
> => {
  const mutationOptions = getPostOpinionsMutationOptions(options);

  return useMutation(mutationOptions);
};
export const putOpinionsId = (
  id: MaybeRef<Uuid>,
  opinionDTO: MaybeRef<OpinionDTO>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<void>> => {
  id = unref(id);
  opinionDTO = unref(opinionDTO);
  return axios.default.put(`http://localhost:9090/opinions/${id}`, opinionDTO, options);
};

export const getPutOpinionsIdMutationOptions = <
  TError = AxiosError<NotFound404Response | ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof putOpinionsId>>,
    TError,
    { id: Uuid; data: OpinionDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof putOpinionsId>>,
  TError,
  { id: Uuid; data: OpinionDTO },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof putOpinionsId>>,
    { id: Uuid; data: OpinionDTO }
  > = (props) => {
    const { id, data } = props ?? {};

    return putOpinionsId(id, data, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type PutOpinionsIdMutationResult = NonNullable<Awaited<ReturnType<typeof putOpinionsId>>>;
export type PutOpinionsIdMutationBody = OpinionDTO;
export type PutOpinionsIdMutationError = AxiosError<
  NotFound404Response | ValidationFailed422Response
>;

export const usePutOpinionsId = <
  TError = AxiosError<NotFound404Response | ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof putOpinionsId>>,
    TError,
    { id: Uuid; data: OpinionDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof putOpinionsId>>,
  TError,
  { id: Uuid; data: OpinionDTO },
  TContext
> => {
  const mutationOptions = getPutOpinionsIdMutationOptions(options);

  return useMutation(mutationOptions);
};
