import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { DeliverEthTestModule } from '../../../test.module';
import { KeyValuPairHolderComponent } from 'app/entities/key-valu-pair-holder/key-valu-pair-holder.component';
import { KeyValuPairHolderService } from 'app/entities/key-valu-pair-holder/key-valu-pair-holder.service';
import { KeyValuPairHolder } from 'app/shared/model/key-valu-pair-holder.model';

describe('Component Tests', () => {
  describe('KeyValuPairHolder Management Component', () => {
    let comp: KeyValuPairHolderComponent;
    let fixture: ComponentFixture<KeyValuPairHolderComponent>;
    let service: KeyValuPairHolderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [KeyValuPairHolderComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: {
                subscribe: (fn: (value: Data) => void) =>
                  fn({
                    pagingParams: {
                      predicate: 'id',
                      reverse: false,
                      page: 0
                    }
                  })
              }
            }
          }
        ]
      })
        .overrideTemplate(KeyValuPairHolderComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(KeyValuPairHolderComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(KeyValuPairHolderService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new KeyValuPairHolder(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.keyValuPairHolders && comp.keyValuPairHolders[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new KeyValuPairHolder(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.keyValuPairHolders && comp.keyValuPairHolders[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
