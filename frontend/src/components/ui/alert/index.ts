import { type VariantProps, cva } from 'class-variance-authority';

export { default as Alert } from './Alert.vue';
export { default as AlertTitle } from './AlertTitle.vue';
export { default as AlertDescription } from './AlertDescription.vue';

export const alertVariants = cva(
  'relative w-full rounded-lg border border-neutral-200 p-4 [&>svg~*]:pl-7 [&>svg+div]:translate-y-[-3px] [&>svg]:absolute [&>svg]:left-4 [&>svg]:top-4 [&>svg]:text-neutral-950 dark:[&>svg]:text-neutral-50',
  {
    variants: {
      variant: {
        default: 'bg-white text-neutral-950 ',
        destructive:
          'border-red-500/50 text-red-500 [&>svg]:text-red-500 dark:[&>svg]:text-red-900',
      },
    },
    defaultVariants: {
      variant: 'default',
    },
  },
);

export type AlertVariants = VariantProps<typeof alertVariants>;
