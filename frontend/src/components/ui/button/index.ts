import { type VariantProps, cva } from 'class-variance-authority';

export { default as Button } from './Button.vue';

export const buttonVariants = cva(
  'inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-white transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-neutral-950 focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 ',
  {
    variants: {
      variant: {
        default: 'bg-primary text-neutral-950 hover:bg-primary/90 ',
        destructive: 'bg-red-500 text-neutral-50 hover:bg-red-500/90 ',
        outline: 'border border-neutral-200 bg-white hover:bg-neutral-100 hover:text-neutral-900 ',
        'outline-destructive':
          'border border-red-500 bg-white text-red-500 hover:bg-red-500/90 hover:text-neutral-50',
        secondary: 'bg-neutral-100 text-neutral-900 hover:bg-neutral-100/80 ',
        ghost: 'hover:bg-neutral-100 hover:text-neutral-900 ',
        link: 'text-neutral-900 underline-offset-4 hover:underline ',
      },
      size: {
        default: 'h-10 px-4 py-2',
        sm: 'h-9 rounded-md px-3',
        lg: 'h-11 rounded-md px-8',
        icon: 'h-10 w-10',
      },
    },
    defaultVariants: {
      variant: 'default',
      size: 'default',
    },
  },
);

export type ButtonVariants = VariantProps<typeof buttonVariants>;
