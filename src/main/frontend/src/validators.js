import errors from './errors';

export const requireRule = (value) => !!value ? undefined : errors.EMPTY;

export const versionRule = (value) => /^\d+\.\d+\.\d+$/.test(value) ? undefined : errors.VERSION;
