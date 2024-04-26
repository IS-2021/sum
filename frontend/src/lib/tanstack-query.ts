import { type QueryClientConfig } from '@tanstack/vue-query';
import { z } from 'zod';

const MAX_QUERY_RETRIES = 4;
const SKIPPED_HTTP_CODES = [401, 402, 403, 404];

const errorShape = z.object({
  response: z.object({
    status: z.number(),
    data: z.unknown(),
    headers: z.unknown(),
  }),
});

/**
 * Custom retry logic. Disabled retries for 4xx status codes.
 * @param failureCount
 * @param error
 */
export const queryRetry = (failureCount: number, error: Error) => {
  if (failureCount > MAX_QUERY_RETRIES) {
    return false;
  }

  const val = errorShape.safeParse(error);

  if (val.success) {
    if (SKIPPED_HTTP_CODES.includes(val.data.response.status)) {
      console.debug('query:retry - short circuited');
      return false;
    }
  }

  return true;
};

export const queryClientConfig: QueryClientConfig = {
  defaultOptions: {
    queries: {
      retry: queryRetry,
    },
  },
};
