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
  BadRequest400Response,
  NotFound404Response,
  PostUsersUserIdAddressParams,
  UserDTO,
  UserInputDTO,
  UserMeDTO,
  Uuid,
  ValidationFailed422Response,
} from '../../api-model';

export const getUsers = (options?: AxiosRequestConfig): Promise<AxiosResponse<UserDTO[]>> => {
  return axios.default.get(`http://localhost:9090/users`, options);
};

export const getGetUsersQueryKey = () => {
  return ['http:', 'localhost:9090', 'users'] as const;
};

export const getGetUsersQueryOptions = <
  TData = Awaited<ReturnType<typeof getUsers>>,
  TError = AxiosError<unknown>,
>(options?: {
  query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getUsers>>, TError, TData>>;
  axios?: AxiosRequestConfig;
}) => {
  const { query: queryOptions, axios: axiosOptions } = options ?? {};

  const queryKey = getGetUsersQueryKey();

  const queryFn: QueryFunction<Awaited<ReturnType<typeof getUsers>>> = ({ signal }) =>
    getUsers({ signal, ...axiosOptions });

  return { queryKey, queryFn, ...queryOptions } as UseQueryOptions<
    Awaited<ReturnType<typeof getUsers>>,
    TError,
    TData
  >;
};

export type GetUsersQueryResult = NonNullable<Awaited<ReturnType<typeof getUsers>>>;
export type GetUsersQueryError = AxiosError<unknown>;

export const useGetUsers = <
  TData = Awaited<ReturnType<typeof getUsers>>,
  TError = AxiosError<unknown>,
>(options?: {
  query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getUsers>>, TError, TData>>;
  axios?: AxiosRequestConfig;
}): UseQueryReturnType<TData, TError> & { queryKey: QueryKey } => {
  const queryOptions = getGetUsersQueryOptions(options);

  const query = useQuery(queryOptions) as UseQueryReturnType<TData, TError> & {
    queryKey: QueryKey;
  };

  query.queryKey = unref(queryOptions).queryKey as QueryKey;

  return query;
};

export const getUsersId = (
  id: MaybeRef<Uuid>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<UserDTO>> => {
  id = unref(id);
  return axios.default.get(`http://localhost:9090/users/${id}`, options);
};

export const getGetUsersIdQueryKey = (id: MaybeRef<Uuid>) => {
  return ['http:', 'localhost:9090', 'users', id] as const;
};

export const getGetUsersIdQueryOptions = <
  TData = Awaited<ReturnType<typeof getUsersId>>,
  TError = AxiosError<NotFound404Response>,
>(
  id: MaybeRef<Uuid>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getUsersId>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
) => {
  const { query: queryOptions, axios: axiosOptions } = options ?? {};

  const queryKey = getGetUsersIdQueryKey(id);

  const queryFn: QueryFunction<Awaited<ReturnType<typeof getUsersId>>> = ({ signal }) =>
    getUsersId(id, { signal, ...axiosOptions });

  return {
    queryKey,
    queryFn,
    enabled: computed(() => !!unref(id)),
    ...queryOptions,
  } as UseQueryOptions<Awaited<ReturnType<typeof getUsersId>>, TError, TData>;
};

export type GetUsersIdQueryResult = NonNullable<Awaited<ReturnType<typeof getUsersId>>>;
export type GetUsersIdQueryError = AxiosError<NotFound404Response>;

export const useGetUsersId = <
  TData = Awaited<ReturnType<typeof getUsersId>>,
  TError = AxiosError<NotFound404Response>,
>(
  id: MaybeRef<Uuid>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getUsersId>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
): UseQueryReturnType<TData, TError> & { queryKey: QueryKey } => {
  const queryOptions = getGetUsersIdQueryOptions(id, options);

  const query = useQuery(queryOptions) as UseQueryReturnType<TData, TError> & {
    queryKey: QueryKey;
  };

  query.queryKey = unref(queryOptions).queryKey as QueryKey;

  return query;
};

export const putUsersId = (
  id: MaybeRef<Uuid>,
  userInputDTO: MaybeRef<UserInputDTO>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<UserDTO>> => {
  id = unref(id);
  userInputDTO = unref(userInputDTO);
  return axios.default.put(`http://localhost:9090/users/${id}`, userInputDTO, options);
};

export const getPutUsersIdMutationOptions = <
  TError = AxiosError<ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof putUsersId>>,
    TError,
    { id: Uuid; data: UserInputDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof putUsersId>>,
  TError,
  { id: Uuid; data: UserInputDTO },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof putUsersId>>,
    { id: Uuid; data: UserInputDTO }
  > = (props) => {
    const { id, data } = props ?? {};

    return putUsersId(id, data, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type PutUsersIdMutationResult = NonNullable<Awaited<ReturnType<typeof putUsersId>>>;
export type PutUsersIdMutationBody = UserInputDTO;
export type PutUsersIdMutationError = AxiosError<ValidationFailed422Response>;

export const usePutUsersId = <
  TError = AxiosError<ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof putUsersId>>,
    TError,
    { id: Uuid; data: UserInputDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof putUsersId>>,
  TError,
  { id: Uuid; data: UserInputDTO },
  TContext
> => {
  const mutationOptions = getPutUsersIdMutationOptions(options);

  return useMutation(mutationOptions);
};
export const deleteUsersId = (
  id: MaybeRef<Uuid>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<void>> => {
  id = unref(id);
  return axios.default.delete(`http://localhost:9090/users/${id}`, options);
};

export const getDeleteUsersIdMutationOptions = <
  TError = AxiosError<NotFound404Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof deleteUsersId>>,
    TError,
    { id: Uuid },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof deleteUsersId>>,
  TError,
  { id: Uuid },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<Awaited<ReturnType<typeof deleteUsersId>>, { id: Uuid }> = (
    props,
  ) => {
    const { id } = props ?? {};

    return deleteUsersId(id, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type DeleteUsersIdMutationResult = NonNullable<Awaited<ReturnType<typeof deleteUsersId>>>;

export type DeleteUsersIdMutationError = AxiosError<NotFound404Response>;

export const useDeleteUsersId = <
  TError = AxiosError<NotFound404Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof deleteUsersId>>,
    TError,
    { id: Uuid },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof deleteUsersId>>,
  TError,
  { id: Uuid },
  TContext
> => {
  const mutationOptions = getDeleteUsersIdMutationOptions(options);

  return useMutation(mutationOptions);
};
export const getUsersMe = (options?: AxiosRequestConfig): Promise<AxiosResponse<UserMeDTO>> => {
  return axios.default.get(`http://localhost:9090/users/me`, options);
};

export const getGetUsersMeQueryKey = () => {
  return ['http:', 'localhost:9090', 'users', 'me'] as const;
};

export const getGetUsersMeQueryOptions = <
  TData = Awaited<ReturnType<typeof getUsersMe>>,
  TError = AxiosError<NotFound404Response>,
>(options?: {
  query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getUsersMe>>, TError, TData>>;
  axios?: AxiosRequestConfig;
}) => {
  const { query: queryOptions, axios: axiosOptions } = options ?? {};

  const queryKey = getGetUsersMeQueryKey();

  const queryFn: QueryFunction<Awaited<ReturnType<typeof getUsersMe>>> = ({ signal }) =>
    getUsersMe({ signal, ...axiosOptions });

  return { queryKey, queryFn, ...queryOptions } as UseQueryOptions<
    Awaited<ReturnType<typeof getUsersMe>>,
    TError,
    TData
  >;
};

export type GetUsersMeQueryResult = NonNullable<Awaited<ReturnType<typeof getUsersMe>>>;
export type GetUsersMeQueryError = AxiosError<NotFound404Response>;

export const useGetUsersMe = <
  TData = Awaited<ReturnType<typeof getUsersMe>>,
  TError = AxiosError<NotFound404Response>,
>(options?: {
  query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getUsersMe>>, TError, TData>>;
  axios?: AxiosRequestConfig;
}): UseQueryReturnType<TData, TError> & { queryKey: QueryKey } => {
  const queryOptions = getGetUsersMeQueryOptions(options);

  const query = useQuery(queryOptions) as UseQueryReturnType<TData, TError> & {
    queryKey: QueryKey;
  };

  query.queryKey = unref(queryOptions).queryKey as QueryKey;

  return query;
};

export const postUsersUserIdAddress = (
  userId: MaybeRef<Uuid>,
  params: MaybeRef<PostUsersUserIdAddressParams>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<UserMeDTO>> => {
  userId = unref(userId);
  params = unref(params);
  return axios.default.post(`http://localhost:9090/users/${userId}/address`, undefined, {
    ...options,
    params: { ...unref(params), ...options?.params },
  });
};

export const getPostUsersUserIdAddressMutationOptions = <
  TError = AxiosError<BadRequest400Response | NotFound404Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof postUsersUserIdAddress>>,
    TError,
    { userId: Uuid; params: PostUsersUserIdAddressParams },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof postUsersUserIdAddress>>,
  TError,
  { userId: Uuid; params: PostUsersUserIdAddressParams },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof postUsersUserIdAddress>>,
    { userId: Uuid; params: PostUsersUserIdAddressParams }
  > = (props) => {
    const { userId, params } = props ?? {};

    return postUsersUserIdAddress(userId, params, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type PostUsersUserIdAddressMutationResult = NonNullable<
  Awaited<ReturnType<typeof postUsersUserIdAddress>>
>;

export type PostUsersUserIdAddressMutationError = AxiosError<
  BadRequest400Response | NotFound404Response
>;

export const usePostUsersUserIdAddress = <
  TError = AxiosError<BadRequest400Response | NotFound404Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof postUsersUserIdAddress>>,
    TError,
    { userId: Uuid; params: PostUsersUserIdAddressParams },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof postUsersUserIdAddress>>,
  TError,
  { userId: Uuid; params: PostUsersUserIdAddressParams },
  TContext
> => {
  const mutationOptions = getPostUsersUserIdAddressMutationOptions(options);

  return useMutation(mutationOptions);
};
