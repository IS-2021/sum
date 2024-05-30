import type { Timestamp } from './api-model';
import { format } from 'date-fns';

export function formatDate(date: Timestamp) {
  return format(new Date(date), "MM/dd/yyyy HH':'mm':'ss");
}
