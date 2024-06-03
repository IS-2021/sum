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
import { unref } from 'vue';
import type { MaybeRef } from 'vue';
import type {
  DeleteOpinionsParams,
  GetOpinionsParams,
  NotFound404Response,
  OpinionDTO,
} from '../../api-model';

export const getOpinions = (
  params: MaybeRef<GetOpinionsParams>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<OpinionDTO>> => {
  params = unref(params);
  return axios.default.get(`http://localhost:9090/opinions`, {
    ...options,
    params: { ...unref(params), ...options?.params },
  });
};

export const getGetOpinionsQueryKey = (params: MaybeRef<GetOpinionsParams>) => {
  return ['http:', 'localhost:9090', 'opinions', ...(params ? [params] : [])] as const;
};

export const getGetOpinionsQueryOptions = <
  TData = Awaited<ReturnType<typeof getOpinions>>,
  TError = AxiosError<NotFound404Response>,
>(
  params: MaybeRef<GetOpinionsParams>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getOpinions>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
) => {
  const { query: queryOptions, axios: axiosOptions } = options ?? {};

  const queryKey = getGetOpinionsQueryKey(params);

  const queryFn: QueryFunction<Awaited<ReturnType<typeof getOpinions>>> = ({ signal }) =>
    getOpinions(params, { signal, ...axiosOptions });

  return { queryKey, queryFn, ...queryOptions } as UseQueryOptions<
    Awaited<ReturnType<typeof getOpinions>>,
    TError,
    TData
  >;
};

export type GetOpinionsQueryResult = NonNullable<Awaited<ReturnType<typeof getOpinions>>>;
export type GetOpinionsQueryError = AxiosError<NotFound404Response>;

export const useGetOpinions = <
  TData = Awaited<ReturnType<typeof getOpinions>>,
  TError = AxiosError<NotFound404Response>,
>(
  params: MaybeRef<GetOpinionsParams>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getOpinions>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
): UseQueryReturnType<TData, TError> & { queryKey: QueryKey } => {
  const queryOptions = getGetOpinionsQueryOptions(params, options);

  const query = useQuery(queryOptions) as UseQueryReturnType<TData, TError> & {
    queryKey: QueryKey;
  };

  query.queryKey = unref(queryOptions).queryKey as QueryKey;

  return query;
};

export const deleteOpinions = (
  params: MaybeRef<DeleteOpinionsParams>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<OpinionDTO>> => {
  params = unref(params);
  return axios.default.delete(`http://localhost:9090/opinions`, {
    ...options,
    params: { ...unref(params), ...options?.params },
  });
};

export const getDeleteOpinionsMutationOptions = <
  TError = AxiosError<NotFound404Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof deleteOpinions>>,
    TError,
    { params: DeleteOpinionsParams },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof deleteOpinions>>,
  TError,
  { params: DeleteOpinionsParams },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof deleteOpinions>>,
    { params: DeleteOpinionsParams }
  > = (props) => {
    const { params } = props ?? {};

    return deleteOpinions(params, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type DeleteOpinionsMutationResult = NonNullable<Awaited<ReturnType<typeof deleteOpinions>>>;

export type DeleteOpinionsMutationError = AxiosError<NotFound404Response>;

export const useDeleteOpinions = <
  TError = AxiosError<NotFound404Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof deleteOpinions>>,
    TError,
    { params: DeleteOpinionsParams },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof deleteOpinions>>,
  TError,
  { params: DeleteOpinionsParams },
  TContext
> => {
  const mutationOptions = getDeleteOpinionsMutationOptions(options);

  return useMutation(mutationOptions);
};
