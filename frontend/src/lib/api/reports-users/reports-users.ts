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
  HttpException,
  NotFound404Response,
  ReportDTO,
  ReportsInputDTO,
  Uuid,
  ValidationFailed422Response,
} from '../../api-model';

export const getReportsUsers = (
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<ReportDTO[]>> => {
  return axios.default.get(`http://localhost:9090/reports/users`, options);
};

export const getGetReportsUsersQueryKey = () => {
  return ['http:', 'localhost:9090', 'reports', 'users'] as const;
};

export const getGetReportsUsersQueryOptions = <
  TData = Awaited<ReturnType<typeof getReportsUsers>>,
  TError = AxiosError<unknown>,
>(options?: {
  query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getReportsUsers>>, TError, TData>>;
  axios?: AxiosRequestConfig;
}) => {
  const { query: queryOptions, axios: axiosOptions } = options ?? {};

  const queryKey = getGetReportsUsersQueryKey();

  const queryFn: QueryFunction<Awaited<ReturnType<typeof getReportsUsers>>> = ({ signal }) =>
    getReportsUsers({ signal, ...axiosOptions });

  return { queryKey, queryFn, ...queryOptions } as UseQueryOptions<
    Awaited<ReturnType<typeof getReportsUsers>>,
    TError,
    TData
  >;
};

export type GetReportsUsersQueryResult = NonNullable<Awaited<ReturnType<typeof getReportsUsers>>>;
export type GetReportsUsersQueryError = AxiosError<unknown>;

export const useGetReportsUsers = <
  TData = Awaited<ReturnType<typeof getReportsUsers>>,
  TError = AxiosError<unknown>,
>(options?: {
  query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getReportsUsers>>, TError, TData>>;
  axios?: AxiosRequestConfig;
}): UseQueryReturnType<TData, TError> & { queryKey: QueryKey } => {
  const queryOptions = getGetReportsUsersQueryOptions(options);

  const query = useQuery(queryOptions) as UseQueryReturnType<TData, TError> & {
    queryKey: QueryKey;
  };

  query.queryKey = unref(queryOptions).queryKey as QueryKey;

  return query;
};

export const postReportsUsers = (
  reportsInputDTO: MaybeRef<ReportsInputDTO>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<ReportDTO>> => {
  reportsInputDTO = unref(reportsInputDTO);
  return axios.default.post(`http://localhost:9090/reports/users`, reportsInputDTO, options);
};

export const getPostReportsUsersMutationOptions = <
  TError = AxiosError<HttpException>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof postReportsUsers>>,
    TError,
    { data: ReportsInputDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof postReportsUsers>>,
  TError,
  { data: ReportsInputDTO },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof postReportsUsers>>,
    { data: ReportsInputDTO }
  > = (props) => {
    const { data } = props ?? {};

    return postReportsUsers(data, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type PostReportsUsersMutationResult = NonNullable<
  Awaited<ReturnType<typeof postReportsUsers>>
>;
export type PostReportsUsersMutationBody = ReportsInputDTO;
export type PostReportsUsersMutationError = AxiosError<HttpException>;

export const usePostReportsUsers = <
  TError = AxiosError<HttpException>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof postReportsUsers>>,
    TError,
    { data: ReportsInputDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof postReportsUsers>>,
  TError,
  { data: ReportsInputDTO },
  TContext
> => {
  const mutationOptions = getPostReportsUsersMutationOptions(options);

  return useMutation(mutationOptions);
};
export const getReportsUsersId = (
  id: MaybeRef<Uuid>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<ReportDTO>> => {
  id = unref(id);
  return axios.default.get(`http://localhost:9090/reports/users/${id}`, options);
};

export const getGetReportsUsersIdQueryKey = (id: MaybeRef<Uuid>) => {
  return ['http:', 'localhost:9090', 'reports', 'users', id] as const;
};

export const getGetReportsUsersIdQueryOptions = <
  TData = Awaited<ReturnType<typeof getReportsUsersId>>,
  TError = AxiosError<NotFound404Response>,
>(
  id: MaybeRef<Uuid>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getReportsUsersId>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
) => {
  const { query: queryOptions, axios: axiosOptions } = options ?? {};

  const queryKey = getGetReportsUsersIdQueryKey(id);

  const queryFn: QueryFunction<Awaited<ReturnType<typeof getReportsUsersId>>> = ({ signal }) =>
    getReportsUsersId(id, { signal, ...axiosOptions });

  return {
    queryKey,
    queryFn,
    enabled: computed(() => !!unref(id)),
    ...queryOptions,
  } as UseQueryOptions<Awaited<ReturnType<typeof getReportsUsersId>>, TError, TData>;
};

export type GetReportsUsersIdQueryResult = NonNullable<
  Awaited<ReturnType<typeof getReportsUsersId>>
>;
export type GetReportsUsersIdQueryError = AxiosError<NotFound404Response>;

export const useGetReportsUsersId = <
  TData = Awaited<ReturnType<typeof getReportsUsersId>>,
  TError = AxiosError<NotFound404Response>,
>(
  id: MaybeRef<Uuid>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getReportsUsersId>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
): UseQueryReturnType<TData, TError> & { queryKey: QueryKey } => {
  const queryOptions = getGetReportsUsersIdQueryOptions(id, options);

  const query = useQuery(queryOptions) as UseQueryReturnType<TData, TError> & {
    queryKey: QueryKey;
  };

  query.queryKey = unref(queryOptions).queryKey as QueryKey;

  return query;
};

export const putReportsUsersId = (
  id: MaybeRef<Uuid>,
  reportDTO: MaybeRef<ReportDTO>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<ReportDTO>> => {
  id = unref(id);
  reportDTO = unref(reportDTO);
  return axios.default.put(`http://localhost:9090/reports/users/${id}`, reportDTO, options);
};

export const getPutReportsUsersIdMutationOptions = <
  TError = AxiosError<NotFound404Response | ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof putReportsUsersId>>,
    TError,
    { id: Uuid; data: ReportDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof putReportsUsersId>>,
  TError,
  { id: Uuid; data: ReportDTO },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof putReportsUsersId>>,
    { id: Uuid; data: ReportDTO }
  > = (props) => {
    const { id, data } = props ?? {};

    return putReportsUsersId(id, data, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type PutReportsUsersIdMutationResult = NonNullable<
  Awaited<ReturnType<typeof putReportsUsersId>>
>;
export type PutReportsUsersIdMutationBody = ReportDTO;
export type PutReportsUsersIdMutationError = AxiosError<
  NotFound404Response | ValidationFailed422Response
>;

export const usePutReportsUsersId = <
  TError = AxiosError<NotFound404Response | ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof putReportsUsersId>>,
    TError,
    { id: Uuid; data: ReportDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof putReportsUsersId>>,
  TError,
  { id: Uuid; data: ReportDTO },
  TContext
> => {
  const mutationOptions = getPutReportsUsersIdMutationOptions(options);

  return useMutation(mutationOptions);
};
export const deleteReportsUsersId = (
  id: MaybeRef<Uuid>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<void>> => {
  id = unref(id);
  return axios.default.delete(`http://localhost:9090/reports/users/${id}`, options);
};

export const getDeleteReportsUsersIdMutationOptions = <
  TError = AxiosError<NotFound404Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof deleteReportsUsersId>>,
    TError,
    { id: Uuid },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof deleteReportsUsersId>>,
  TError,
  { id: Uuid },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof deleteReportsUsersId>>,
    { id: Uuid }
  > = (props) => {
    const { id } = props ?? {};

    return deleteReportsUsersId(id, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type DeleteReportsUsersIdMutationResult = NonNullable<
  Awaited<ReturnType<typeof deleteReportsUsersId>>
>;

export type DeleteReportsUsersIdMutationError = AxiosError<NotFound404Response>;

export const useDeleteReportsUsersId = <
  TError = AxiosError<NotFound404Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof deleteReportsUsersId>>,
    TError,
    { id: Uuid },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof deleteReportsUsersId>>,
  TError,
  { id: Uuid },
  TContext
> => {
  const mutationOptions = getDeleteReportsUsersIdMutationOptions(options);

  return useMutation(mutationOptions);
};
