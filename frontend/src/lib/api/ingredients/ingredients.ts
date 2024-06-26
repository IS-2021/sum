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
  GetIngredientsParams,
  IngredientDTO,
  IngredientInputDTO,
  NotFound404Response,
  PostIngredientsParams,
  Uuid,
  ValidationFailed422Response,
} from '../../api-model';

export const getIngredients = (
  params?: MaybeRef<GetIngredientsParams>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<IngredientDTO[]>> => {
  params = unref(params);
  return axios.default.get(`http://localhost:9090/ingredients`, {
    ...options,
    params: { ...unref(params), ...options?.params },
  });
};

export const getGetIngredientsQueryKey = (params?: MaybeRef<GetIngredientsParams>) => {
  return ['http:', 'localhost:9090', 'ingredients', ...(params ? [params] : [])] as const;
};

export const getGetIngredientsQueryOptions = <
  TData = Awaited<ReturnType<typeof getIngredients>>,
  TError = AxiosError<unknown>,
>(
  params?: MaybeRef<GetIngredientsParams>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getIngredients>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
) => {
  const { query: queryOptions, axios: axiosOptions } = options ?? {};

  const queryKey = getGetIngredientsQueryKey(params);

  const queryFn: QueryFunction<Awaited<ReturnType<typeof getIngredients>>> = ({ signal }) =>
    getIngredients(params, { signal, ...axiosOptions });

  return { queryKey, queryFn, ...queryOptions } as UseQueryOptions<
    Awaited<ReturnType<typeof getIngredients>>,
    TError,
    TData
  >;
};

export type GetIngredientsQueryResult = NonNullable<Awaited<ReturnType<typeof getIngredients>>>;
export type GetIngredientsQueryError = AxiosError<unknown>;

export const useGetIngredients = <
  TData = Awaited<ReturnType<typeof getIngredients>>,
  TError = AxiosError<unknown>,
>(
  params?: MaybeRef<GetIngredientsParams>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getIngredients>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
): UseQueryReturnType<TData, TError> & { queryKey: QueryKey } => {
  const queryOptions = getGetIngredientsQueryOptions(params, options);

  const query = useQuery(queryOptions) as UseQueryReturnType<TData, TError> & {
    queryKey: QueryKey;
  };

  query.queryKey = unref(queryOptions).queryKey as QueryKey;

  return query;
};

export const postIngredients = (
  ingredientInputDTO: MaybeRef<IngredientInputDTO>,
  params?: MaybeRef<PostIngredientsParams>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<IngredientDTO>> => {
  ingredientInputDTO = unref(ingredientInputDTO);
  params = unref(params);
  return axios.default.post(`http://localhost:9090/ingredients`, ingredientInputDTO, {
    ...options,
    params: { ...unref(params), ...options?.params },
  });
};

export const getPostIngredientsMutationOptions = <
  TError = AxiosError<ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof postIngredients>>,
    TError,
    { data: IngredientInputDTO; params?: PostIngredientsParams },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof postIngredients>>,
  TError,
  { data: IngredientInputDTO; params?: PostIngredientsParams },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof postIngredients>>,
    { data: IngredientInputDTO; params?: PostIngredientsParams }
  > = (props) => {
    const { data, params } = props ?? {};

    return postIngredients(data, params, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type PostIngredientsMutationResult = NonNullable<
  Awaited<ReturnType<typeof postIngredients>>
>;
export type PostIngredientsMutationBody = IngredientInputDTO;
export type PostIngredientsMutationError = AxiosError<ValidationFailed422Response>;

export const usePostIngredients = <
  TError = AxiosError<ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof postIngredients>>,
    TError,
    { data: IngredientInputDTO; params?: PostIngredientsParams },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof postIngredients>>,
  TError,
  { data: IngredientInputDTO; params?: PostIngredientsParams },
  TContext
> => {
  const mutationOptions = getPostIngredientsMutationOptions(options);

  return useMutation(mutationOptions);
};
export const getIngredientsId = (
  id: MaybeRef<Uuid>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<IngredientDTO>> => {
  id = unref(id);
  return axios.default.get(`http://localhost:9090/ingredients/${id}`, options);
};

export const getGetIngredientsIdQueryKey = (id: MaybeRef<Uuid>) => {
  return ['http:', 'localhost:9090', 'ingredients', id] as const;
};

export const getGetIngredientsIdQueryOptions = <
  TData = Awaited<ReturnType<typeof getIngredientsId>>,
  TError = AxiosError<NotFound404Response>,
>(
  id: MaybeRef<Uuid>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getIngredientsId>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
) => {
  const { query: queryOptions, axios: axiosOptions } = options ?? {};

  const queryKey = getGetIngredientsIdQueryKey(id);

  const queryFn: QueryFunction<Awaited<ReturnType<typeof getIngredientsId>>> = ({ signal }) =>
    getIngredientsId(id, { signal, ...axiosOptions });

  return {
    queryKey,
    queryFn,
    enabled: computed(() => !!unref(id)),
    ...queryOptions,
  } as UseQueryOptions<Awaited<ReturnType<typeof getIngredientsId>>, TError, TData>;
};

export type GetIngredientsIdQueryResult = NonNullable<Awaited<ReturnType<typeof getIngredientsId>>>;
export type GetIngredientsIdQueryError = AxiosError<NotFound404Response>;

export const useGetIngredientsId = <
  TData = Awaited<ReturnType<typeof getIngredientsId>>,
  TError = AxiosError<NotFound404Response>,
>(
  id: MaybeRef<Uuid>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getIngredientsId>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
): UseQueryReturnType<TData, TError> & { queryKey: QueryKey } => {
  const queryOptions = getGetIngredientsIdQueryOptions(id, options);

  const query = useQuery(queryOptions) as UseQueryReturnType<TData, TError> & {
    queryKey: QueryKey;
  };

  query.queryKey = unref(queryOptions).queryKey as QueryKey;

  return query;
};

export const putIngredientsId = (
  id: MaybeRef<Uuid>,
  ingredientInputDTO: MaybeRef<IngredientInputDTO>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<IngredientDTO>> => {
  id = unref(id);
  ingredientInputDTO = unref(ingredientInputDTO);
  return axios.default.put(`http://localhost:9090/ingredients/${id}`, ingredientInputDTO, options);
};

export const getPutIngredientsIdMutationOptions = <
  TError = AxiosError<NotFound404Response | ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof putIngredientsId>>,
    TError,
    { id: Uuid; data: IngredientInputDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof putIngredientsId>>,
  TError,
  { id: Uuid; data: IngredientInputDTO },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof putIngredientsId>>,
    { id: Uuid; data: IngredientInputDTO }
  > = (props) => {
    const { id, data } = props ?? {};

    return putIngredientsId(id, data, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type PutIngredientsIdMutationResult = NonNullable<
  Awaited<ReturnType<typeof putIngredientsId>>
>;
export type PutIngredientsIdMutationBody = IngredientInputDTO;
export type PutIngredientsIdMutationError = AxiosError<
  NotFound404Response | ValidationFailed422Response
>;

export const usePutIngredientsId = <
  TError = AxiosError<NotFound404Response | ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof putIngredientsId>>,
    TError,
    { id: Uuid; data: IngredientInputDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof putIngredientsId>>,
  TError,
  { id: Uuid; data: IngredientInputDTO },
  TContext
> => {
  const mutationOptions = getPutIngredientsIdMutationOptions(options);

  return useMutation(mutationOptions);
};
export const deleteIngredientsId = (
  id: MaybeRef<Uuid>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<void>> => {
  id = unref(id);
  return axios.default.delete(`http://localhost:9090/ingredients/${id}`, options);
};

export const getDeleteIngredientsIdMutationOptions = <
  TError = AxiosError<NotFound404Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof deleteIngredientsId>>,
    TError,
    { id: Uuid },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof deleteIngredientsId>>,
  TError,
  { id: Uuid },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof deleteIngredientsId>>,
    { id: Uuid }
  > = (props) => {
    const { id } = props ?? {};

    return deleteIngredientsId(id, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type DeleteIngredientsIdMutationResult = NonNullable<
  Awaited<ReturnType<typeof deleteIngredientsId>>
>;

export type DeleteIngredientsIdMutationError = AxiosError<NotFound404Response>;

export const useDeleteIngredientsId = <
  TError = AxiosError<NotFound404Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof deleteIngredientsId>>,
    TError,
    { id: Uuid },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof deleteIngredientsId>>,
  TError,
  { id: Uuid },
  TContext
> => {
  const mutationOptions = getDeleteIngredientsIdMutationOptions(options);

  return useMutation(mutationOptions);
};
