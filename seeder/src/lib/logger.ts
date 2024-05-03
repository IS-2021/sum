import { pino } from "pino";

const logger = pino();

export function logEvent({ msg, data }: { msg: string; data?: any }) {
  logger.info({
    msg,
    data,
  });
}
