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
  GetMealsParams,
  MealDTO,
  MealInputDTO,
  NotFound404Response,
  Uuid,
  ValidationFailed422Response,
} from '../../api-model';

export const getMeals = (
  params?: MaybeRef<GetMealsParams>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<MealDTO[]>> => {
  params = unref(params);
  return axios.default.get(`http://localhost:9090/meals`, {
    ...options,
    params: { ...unref(params), ...options?.params },
  });
};

export const getGetMealsQueryKey = (params?: MaybeRef<GetMealsParams>) => {
  return ['http:', 'localhost:9090', 'meals', ...(params ? [params] : [])] as const;
};

export const getGetMealsQueryOptions = <
  TData = Awaited<ReturnType<typeof getMeals>>,
  TError = AxiosError<unknown>,
>(
  params?: MaybeRef<GetMealsParams>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getMeals>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
) => {
  const { query: queryOptions, axios: axiosOptions } = options ?? {};

  const queryKey = getGetMealsQueryKey(params);

  const queryFn: QueryFunction<Awaited<ReturnType<typeof getMeals>>> = ({ signal }) =>
    getMeals(params, { signal, ...axiosOptions });

  return { queryKey, queryFn, ...queryOptions } as UseQueryOptions<
    Awaited<ReturnType<typeof getMeals>>,
    TError,
    TData
  >;
};

export type GetMealsQueryResult = NonNullable<Awaited<ReturnType<typeof getMeals>>>;
export type GetMealsQueryError = AxiosError<unknown>;

export const useGetMeals = <
  TData = Awaited<ReturnType<typeof getMeals>>,
  TError = AxiosError<unknown>,
>(
  params?: MaybeRef<GetMealsParams>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getMeals>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
): UseQueryReturnType<TData, TError> & { queryKey: QueryKey } => {
  const queryOptions = getGetMealsQueryOptions(params, options);

  const query = useQuery(queryOptions) as UseQueryReturnType<TData, TError> & {
    queryKey: QueryKey;
  };

  query.queryKey = unref(queryOptions).queryKey as QueryKey;

  return query;
};

export const postMeals = (
  mealInputDTO: MaybeRef<MealInputDTO>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<MealDTO>> => {
  mealInputDTO = unref(mealInputDTO);
  return axios.default.post(`http://localhost:9090/meals`, mealInputDTO, options);
};

export const getPostMealsMutationOptions = <
  TError = AxiosError<ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof postMeals>>,
    TError,
    { data: MealInputDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof postMeals>>,
  TError,
  { data: MealInputDTO },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof postMeals>>,
    { data: MealInputDTO }
  > = (props) => {
    const { data } = props ?? {};

    return postMeals(data, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type PostMealsMutationResult = NonNullable<Awaited<ReturnType<typeof postMeals>>>;
export type PostMealsMutationBody = MealInputDTO;
export type PostMealsMutationError = AxiosError<ValidationFailed422Response>;

export const usePostMeals = <
  TError = AxiosError<ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof postMeals>>,
    TError,
    { data: MealInputDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof postMeals>>,
  TError,
  { data: MealInputDTO },
  TContext
> => {
  const mutationOptions = getPostMealsMutationOptions(options);

  return useMutation(mutationOptions);
};
export const getMealsId = (
  id: MaybeRef<Uuid>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<MealDTO>> => {
  id = unref(id);
  return axios.default.get(`http://localhost:9090/meals/${id}`, options);
};

export const getGetMealsIdQueryKey = (id: MaybeRef<Uuid>) => {
  return ['http:', 'localhost:9090', 'meals', id] as const;
};

export const getGetMealsIdQueryOptions = <
  TData = Awaited<ReturnType<typeof getMealsId>>,
  TError = AxiosError<NotFound404Response>,
>(
  id: MaybeRef<Uuid>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getMealsId>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
) => {
  const { query: queryOptions, axios: axiosOptions } = options ?? {};

  const queryKey = getGetMealsIdQueryKey(id);

  const queryFn: QueryFunction<Awaited<ReturnType<typeof getMealsId>>> = ({ signal }) =>
    getMealsId(id, { signal, ...axiosOptions });

  return {
    queryKey,
    queryFn,
    enabled: computed(() => !!unref(id)),
    ...queryOptions,
  } as UseQueryOptions<Awaited<ReturnType<typeof getMealsId>>, TError, TData>;
};

export type GetMealsIdQueryResult = NonNullable<Awaited<ReturnType<typeof getMealsId>>>;
export type GetMealsIdQueryError = AxiosError<NotFound404Response>;

export const useGetMealsId = <
  TData = Awaited<ReturnType<typeof getMealsId>>,
  TError = AxiosError<NotFound404Response>,
>(
  id: MaybeRef<Uuid>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getMealsId>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
): UseQueryReturnType<TData, TError> & { queryKey: QueryKey } => {
  const queryOptions = getGetMealsIdQueryOptions(id, options);

  const query = useQuery(queryOptions) as UseQueryReturnType<TData, TError> & {
    queryKey: QueryKey;
  };

  query.queryKey = unref(queryOptions).queryKey as QueryKey;

  return query;
};

export const putMealsId = (
  id: MaybeRef<Uuid>,
  mealDTO: MaybeRef<MealDTO>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<void>> => {
  id = unref(id);
  mealDTO = unref(mealDTO);
  return axios.default.put(`http://localhost:9090/meals/${id}`, mealDTO, options);
};

export const getPutMealsIdMutationOptions = <
  TError = AxiosError<NotFound404Response | ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof putMealsId>>,
    TError,
    { id: Uuid; data: MealDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof putMealsId>>,
  TError,
  { id: Uuid; data: MealDTO },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof putMealsId>>,
    { id: Uuid; data: MealDTO }
  > = (props) => {
    const { id, data } = props ?? {};

    return putMealsId(id, data, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type PutMealsIdMutationResult = NonNullable<Awaited<ReturnType<typeof putMealsId>>>;
export type PutMealsIdMutationBody = MealDTO;
export type PutMealsIdMutationError = AxiosError<NotFound404Response | ValidationFailed422Response>;

export const usePutMealsId = <
  TError = AxiosError<NotFound404Response | ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof putMealsId>>,
    TError,
    { id: Uuid; data: MealDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof putMealsId>>,
  TError,
  { id: Uuid; data: MealDTO },
  TContext
> => {
  const mutationOptions = getPutMealsIdMutationOptions(options);

  return useMutation(mutationOptions);
};
export const deleteMealsId = (
  id: MaybeRef<Uuid>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<void>> => {
  id = unref(id);
  return axios.default.delete(`http://localhost:9090/meals/${id}`, options);
};

export const getDeleteMealsIdMutationOptions = <
  TError = AxiosError<NotFound404Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof deleteMealsId>>,
    TError,
    { id: Uuid },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof deleteMealsId>>,
  TError,
  { id: Uuid },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<Awaited<ReturnType<typeof deleteMealsId>>, { id: Uuid }> = (
    props,
  ) => {
    const { id } = props ?? {};

    return deleteMealsId(id, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type DeleteMealsIdMutationResult = NonNullable<Awaited<ReturnType<typeof deleteMealsId>>>;

export type DeleteMealsIdMutationError = AxiosError<NotFound404Response>;

export const useDeleteMealsId = <
  TError = AxiosError<NotFound404Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof deleteMealsId>>,
    TError,
    { id: Uuid },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof deleteMealsId>>,
  TError,
  { id: Uuid },
  TContext
> => {
  const mutationOptions = getDeleteMealsIdMutationOptions(options);

  return useMutation(mutationOptions);
};
