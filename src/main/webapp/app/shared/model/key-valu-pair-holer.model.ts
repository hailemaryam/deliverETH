export interface IKeyValuPairHoler {
  id?: number;
  key?: string;
  valueString?: string;
  valueNumber?: number;
  valueImageContentType?: string;
  valueImage?: any;
  valueBlobContentType?: string;
  valueBlob?: any;
}

export class KeyValuPairHoler implements IKeyValuPairHoler {
  constructor(
    public id?: number,
    public key?: string,
    public valueString?: string,
    public valueNumber?: number,
    public valueImageContentType?: string,
    public valueImage?: any,
    public valueBlobContentType?: string,
    public valueBlob?: any
  ) {}
}
