import { SidebarComponent } from "./sidebar.component"
import { StorageService } from '../services/storageService';
import {  BOARDS_DATA } from '../services/storageService.spec';
import { ComponentFixture, fakeAsync, TestBed, tick } from "@angular/core/testing";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { boardKey } from '../../../../../../kanban/src/app/constants';

describe('test sidebar component', () => {
    let fixture: ComponentFixture<SidebarComponent>;
    let component: SidebarComponent;
    let storageService: StorageService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [SidebarComponent],
            imports: [
                FontAwesomeModule,
                FormsModule,
                ReactiveFormsModule
              ],
              providers: [
                StorageService
              ]
        });
        fixture = TestBed.createComponent(SidebarComponent);
        component = fixture.componentInstance;
        storageService = TestBed.inject(StorageService);
    });

    afterEach(() => {
        storageService.clear();
    });

    it('should display sidebar component', () => {
        expect(component).toBeDefined();
    });

    it('number of boards in localStorage should be equal to number of boards set', () => {
        storageService.set(boardKey, BOARDS_DATA);

        component.ngOnInit();

        const boards= storageService.get(boardKey);
        expect(component.boardsCount).toEqual(boards.length)
    });

    it('should set active board', fakeAsync (() => {
        storageService.set(boardKey, BOARDS_DATA);

        component.ngOnInit();
        fixture.detectChanges();

        spyOn(component, 'setActiveBoard');
        let btn = fixture.debugElement.nativeElement.querySelector('.nav-link');
        btn.click();
        tick();
        expect(component.setActiveBoard).toHaveBeenCalled();
    }));
});
