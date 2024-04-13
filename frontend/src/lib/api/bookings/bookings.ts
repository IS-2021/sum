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
  BookingDTO,
  BookingInputDTO,
  NotFound404Response,
  Uuid,
  ValidationFailed422Response,
} from '../../api-model';

export const getBookingsId = (
  id: MaybeRef<Uuid>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<BookingDTO>> => {
  id = unref(id);
  return axios.default.get(`http://localhost:9090/bookings/${id}`, options);
};

export const getGetBookingsIdQueryKey = (id: MaybeRef<Uuid>) => {
  return ['http:', 'localhost:9090', 'bookings', id] as const;
};

export const getGetBookingsIdQueryOptions = <
  TData = Awaited<ReturnType<typeof getBookingsId>>,
  TError = AxiosError<NotFound404Response>,
>(
  id: MaybeRef<Uuid>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getBookingsId>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
) => {
  const { query: queryOptions, axios: axiosOptions } = options ?? {};

  const queryKey = getGetBookingsIdQueryKey(id);

  const queryFn: QueryFunction<Awaited<ReturnType<typeof getBookingsId>>> = ({ signal }) =>
    getBookingsId(id, { signal, ...axiosOptions });

  return {
    queryKey,
    queryFn,
    enabled: computed(() => !!unref(id)),
    ...queryOptions,
  } as UseQueryOptions<Awaited<ReturnType<typeof getBookingsId>>, TError, TData>;
};

export type GetBookingsIdQueryResult = NonNullable<Awaited<ReturnType<typeof getBookingsId>>>;
export type GetBookingsIdQueryError = AxiosError<NotFound404Response>;

export const useGetBookingsId = <
  TData = Awaited<ReturnType<typeof getBookingsId>>,
  TError = AxiosError<NotFound404Response>,
>(
  id: MaybeRef<Uuid>,
  options?: {
    query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getBookingsId>>, TError, TData>>;
    axios?: AxiosRequestConfig;
  },
): UseQueryReturnType<TData, TError> & { queryKey: QueryKey } => {
  const queryOptions = getGetBookingsIdQueryOptions(id, options);

  const query = useQuery(queryOptions) as UseQueryReturnType<TData, TError> & {
    queryKey: QueryKey;
  };

  query.queryKey = unref(queryOptions).queryKey as QueryKey;

  return query;
};

export const putBookingsId = (
  id: MaybeRef<Uuid>,
  bookingDTO: MaybeRef<BookingDTO>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<BookingDTO>> => {
  id = unref(id);
  bookingDTO = unref(bookingDTO);
  return axios.default.put(`http://localhost:9090/bookings/${id}`, bookingDTO, options);
};

export const getPutBookingsIdMutationOptions = <
  TError = AxiosError<NotFound404Response | ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof putBookingsId>>,
    TError,
    { id: Uuid; data: BookingDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof putBookingsId>>,
  TError,
  { id: Uuid; data: BookingDTO },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof putBookingsId>>,
    { id: Uuid; data: BookingDTO }
  > = (props) => {
    const { id, data } = props ?? {};

    return putBookingsId(id, data, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type PutBookingsIdMutationResult = NonNullable<Awaited<ReturnType<typeof putBookingsId>>>;
export type PutBookingsIdMutationBody = BookingDTO;
export type PutBookingsIdMutationError = AxiosError<
  NotFound404Response | ValidationFailed422Response
>;

export const usePutBookingsId = <
  TError = AxiosError<NotFound404Response | ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof putBookingsId>>,
    TError,
    { id: Uuid; data: BookingDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof putBookingsId>>,
  TError,
  { id: Uuid; data: BookingDTO },
  TContext
> => {
  const mutationOptions = getPutBookingsIdMutationOptions(options);

  return useMutation(mutationOptions);
};
export const deleteBookingsId = (
  id: MaybeRef<Uuid>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<void>> => {
  id = unref(id);
  return axios.default.delete(`http://localhost:9090/bookings/${id}`, options);
};

export const getDeleteBookingsIdMutationOptions = <
  TError = AxiosError<NotFound404Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof deleteBookingsId>>,
    TError,
    { id: Uuid },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof deleteBookingsId>>,
  TError,
  { id: Uuid },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<Awaited<ReturnType<typeof deleteBookingsId>>, { id: Uuid }> = (
    props,
  ) => {
    const { id } = props ?? {};

    return deleteBookingsId(id, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type DeleteBookingsIdMutationResult = NonNullable<
  Awaited<ReturnType<typeof deleteBookingsId>>
>;

export type DeleteBookingsIdMutationError = AxiosError<NotFound404Response>;

export const useDeleteBookingsId = <
  TError = AxiosError<NotFound404Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof deleteBookingsId>>,
    TError,
    { id: Uuid },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof deleteBookingsId>>,
  TError,
  { id: Uuid },
  TContext
> => {
  const mutationOptions = getDeleteBookingsIdMutationOptions(options);

  return useMutation(mutationOptions);
};
export const getBookings = (options?: AxiosRequestConfig): Promise<AxiosResponse<BookingDTO[]>> => {
  return axios.default.get(`http://localhost:9090/bookings`, options);
};

export const getGetBookingsQueryKey = () => {
  return ['http:', 'localhost:9090', 'bookings'] as const;
};

export const getGetBookingsQueryOptions = <
  TData = Awaited<ReturnType<typeof getBookings>>,
  TError = AxiosError<unknown>,
>(options?: {
  query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getBookings>>, TError, TData>>;
  axios?: AxiosRequestConfig;
}) => {
  const { query: queryOptions, axios: axiosOptions } = options ?? {};

  const queryKey = getGetBookingsQueryKey();

  const queryFn: QueryFunction<Awaited<ReturnType<typeof getBookings>>> = ({ signal }) =>
    getBookings({ signal, ...axiosOptions });

  return { queryKey, queryFn, ...queryOptions } as UseQueryOptions<
    Awaited<ReturnType<typeof getBookings>>,
    TError,
    TData
  >;
};

export type GetBookingsQueryResult = NonNullable<Awaited<ReturnType<typeof getBookings>>>;
export type GetBookingsQueryError = AxiosError<unknown>;

export const useGetBookings = <
  TData = Awaited<ReturnType<typeof getBookings>>,
  TError = AxiosError<unknown>,
>(options?: {
  query?: Partial<UseQueryOptions<Awaited<ReturnType<typeof getBookings>>, TError, TData>>;
  axios?: AxiosRequestConfig;
}): UseQueryReturnType<TData, TError> & { queryKey: QueryKey } => {
  const queryOptions = getGetBookingsQueryOptions(options);

  const query = useQuery(queryOptions) as UseQueryReturnType<TData, TError> & {
    queryKey: QueryKey;
  };

  query.queryKey = unref(queryOptions).queryKey as QueryKey;

  return query;
};

export const postBookings = (
  bookingInputDTO: MaybeRef<BookingInputDTO>,
  options?: AxiosRequestConfig,
): Promise<AxiosResponse<BookingDTO>> => {
  bookingInputDTO = unref(bookingInputDTO);
  return axios.default.post(`http://localhost:9090/bookings`, bookingInputDTO, options);
};

export const getPostBookingsMutationOptions = <
  TError = AxiosError<ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof postBookings>>,
    TError,
    { data: BookingInputDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationOptions<
  Awaited<ReturnType<typeof postBookings>>,
  TError,
  { data: BookingInputDTO },
  TContext
> => {
  const { mutation: mutationOptions, axios: axiosOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof postBookings>>,
    { data: BookingInputDTO }
  > = (props) => {
    const { data } = props ?? {};

    return postBookings(data, axiosOptions);
  };

  return { mutationFn, ...mutationOptions };
};

export type PostBookingsMutationResult = NonNullable<Awaited<ReturnType<typeof postBookings>>>;
export type PostBookingsMutationBody = BookingInputDTO;
export type PostBookingsMutationError = AxiosError<ValidationFailed422Response>;

export const usePostBookings = <
  TError = AxiosError<ValidationFailed422Response>,
  TContext = unknown,
>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof postBookings>>,
    TError,
    { data: BookingInputDTO },
    TContext
  >;
  axios?: AxiosRequestConfig;
}): UseMutationReturnType<
  Awaited<ReturnType<typeof postBookings>>,
  TError,
  { data: BookingInputDTO },
  TContext
> => {
  const mutationOptions = getPostBookingsMutationOptions(options);

  return useMutation(mutationOptions);
};
