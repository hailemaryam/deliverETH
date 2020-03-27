import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { DeliverEthTestModule } from '../../../test.module';
import { KeyValuPairHolerComponent } from 'app/entities/key-valu-pair-holer/key-valu-pair-holer.component';
import { KeyValuPairHolerService } from 'app/entities/key-valu-pair-holer/key-valu-pair-holer.service';
import { KeyValuPairHoler } from 'app/shared/model/key-valu-pair-holer.model';

describe('Component Tests', () => {
  describe('KeyValuPairHoler Management Component', () => {
    let comp: KeyValuPairHolerComponent;
    let fixture: ComponentFixture<KeyValuPairHolerComponent>;
    let service: KeyValuPairHolerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DeliverEthTestModule],
        declarations: [KeyValuPairHolerComponent],
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
        .overrideTemplate(KeyValuPairHolerComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(KeyValuPairHolerComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(KeyValuPairHolerService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new KeyValuPairHoler(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.keyValuPairHolers && comp.keyValuPairHolers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new KeyValuPairHoler(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.keyValuPairHolers && comp.keyValuPairHolers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
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
