import errors from './errors';

export const notEmpty = (value) => !!value ? undefined : errors.EMPTY;

export const validVersion = (value) => /^\d+\.\d+\.\d+$/.test(value) ? undefined : errors.VERSION;
