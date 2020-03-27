export interface IKeyValuPairHolder {
  id?: number;
  key?: string;
  valueString?: string;
  valueNumber?: number;
  valueImageContentType?: string;
  valueImage?: any;
}

export class KeyValuPairHolder implements IKeyValuPairHolder {
  constructor(
    public id?: number,
    public key?: string,
    public valueString?: string,
    public valueNumber?: number,
    public valueImageContentType?: string,
    public valueImage?: any
  ) {}
}
